package com.example.recipease.features.edit_recipe

import android.R
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.databinding.FragmentAddRecipeBinding
import com.example.recipease.features.add_recipe.AddRecipeViewModel
import com.example.recipease.features.add_recipe.IngredientsViewAdapter
import com.example.recipease.features.add_recipe.StepsViewAdapter
import com.example.recipease.features.recipes_feed.tagsViewAdapter
import com.example.recipease.model.Ingredient
import com.example.recipease.model.Recipe
import com.example.recipease.utils.asFormField
import com.example.recipease.utils.asImageField
import com.example.recipease.utils.asListField
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.example.recipease.utils.FormValidator
import com.example.recipease.utils.asFormField
import com.example.recipease.utils.asImageField
import com.example.recipease.utils.asListField
import com.squareup.picasso.Picasso
import kotlin.getValue

class EditRecipeFragment : Fragment() {

    private lateinit var binding: FragmentAddRecipeBinding
    private val args: EditRecipeFragmentArgs by navArgs()
    private lateinit var ingredientsAdapter: IngredientsViewAdapter
    private lateinit var stepsAdapter: StepsViewAdapter
    private val viewModel: AddRecipeViewModel by viewModels()
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var recipeTags: List<String> = emptyList()
    private var recipeSteps: List<String> = emptyList()
    private var recipeIngredients: List<Ingredient> = emptyList()
    private lateinit var currentRecipe: Recipe
    private lateinit var tagsAdapter: tagsViewAdapter
    private var formValidator = FormValidator()

    private val difficulties = listOf("Easy", "Medium", "Hard")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                binding.ivPhoto.setImageBitmap(it)
                binding.ivCamera.visibility = View.GONE
                binding.tvAddPhoto.visibility = View.GONE
            } ?: Toast.makeText(context, "No image captured", Toast.LENGTH_SHORT).show()
        }

        binding.photoPicker.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentRecipe = args.recipe
        recipeSteps = currentRecipe.steps
        recipeIngredients = currentRecipe.ingredients
        recipeTags = currentRecipe.tags

        binding.loadingIndicator.visibility = View.GONE
        setupTags()
        setupDifficulty()
        setupIngredients()
        setupSteps()
        binding.instructionsRecycler.adapter = stepsAdapter
        binding.instructionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.publishBtn.setOnClickListener {
            addRecipe()
        }
        observeTags()
        viewModel.refreshTags()
        viewModel.connectedUser.observe(viewLifecycleOwner) {}
        populateFields()
        validateForm()
    }

    private fun setupTags() {
        binding.tagsRecycler.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        binding.tagsRecycler.isNestedScrollingEnabled = false

        tagsAdapter = tagsViewAdapter(emptyList()) { selectedTags ->
            recipeTags = selectedTags.toList()
        }
        binding.tagsRecycler.adapter = tagsAdapter
    }

    private fun setupDifficulty() {
        binding.difficulty.setAdapter(ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_1,
            difficulties
        ))
    }

    private fun setupIngredients() {
        ingredientsAdapter = IngredientsViewAdapter(currentRecipe.ingredients.toMutableList()) { updatedIngredients ->
            recipeIngredients = updatedIngredients.toList()
        }
        binding.ingredientsRecycler.adapter = ingredientsAdapter
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.addIngredientBtn.setOnClickListener {
            ingredientsAdapter.addIngredient()
            binding.ingredientsRecycler.scrollToPosition(ingredientsAdapter.itemCount - 1)
        }
    }

    private fun setupSteps() {
        stepsAdapter = StepsViewAdapter(currentRecipe.steps.toMutableList()) { updatedSteps ->
            recipeSteps = updatedSteps.toList()
        }
        binding.addStepBtn.setOnClickListener {
            stepsAdapter.addStep()
            binding.instructionsRecycler.scrollToPosition(stepsAdapter.itemCount - 1)
        }
    }

    private fun addRecipe() {
        binding.loadingIndicator.visibility = View.VISIBLE

        val recipe = Recipe(
            id = currentRecipe.id,
            name = binding.recipeTitle.text.toString(),
            description = binding.recipeDescription.text.toString(),
            time = binding.cookTime.text.toString(),
            servings = binding.servings.text.toString().toIntOrNull() ?: 0,
            difficulty = binding.difficulty.text.toString(),
            userId = viewModel.connectedUser.value?.id ?: "",
            tags = recipeTags,
            steps = recipeSteps,
            ingredients = recipeIngredients,
            pictureUrl = "",
            notes = binding.notes.text.toString(),
            lastUpdated = null
        )

        val imageBitmap = (binding.ivPhoto.drawable as BitmapDrawable).bitmap

        RecipeRepository.shared.addRecipe(recipe, imageBitmap) {
            dismiss()
            binding.loadingIndicator.visibility = View.GONE
        }
    }

    private fun populateFields() {
        binding.recipeTitle.setText(currentRecipe.name)
        binding.recipeDescription.setText(currentRecipe.description)
        binding.cookTime.setText(currentRecipe.time)
        binding.servings.setText(currentRecipe.servings.toString())
        binding.difficulty.setText(currentRecipe.difficulty, false)
        binding.notes.setText(currentRecipe.notes)

        currentRecipe.pictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get().load(it).into(binding.ivPhoto)
                binding.ivCamera.visibility = View.GONE
                binding.tvAddPhoto.visibility = View.GONE
                formValidator.update()
            }
        }
    }

    private fun observeTags() {
        var selectionApplied = false
        viewModel.tags.observe(viewLifecycleOwner) { tagList ->
            tagsAdapter.updateList(tagList)
            if (!selectionApplied) {
                tagsAdapter.updateSelected(currentRecipe.tags.toSet())
                selectionApplied = true
            }
        }
    }

    private fun validateForm() {

        formValidator.addField(binding.recipeTitle.asFormField { it.isNotBlank() })
        formValidator.addField(binding.recipeDescription.asFormField { it.isNotBlank() })
        formValidator.addField(binding.cookTime.asFormField { it.isNotBlank() })
        formValidator.addField(binding.servings.asFormField { it.toIntOrNull()?.let { n -> n in 1..<100 } == true })
        formValidator.addField(binding.difficulty.asFormField { it.isNotBlank() })
        formValidator.addField(binding.notes.asFormField { it.isNotBlank() })

        val ingredientsField = binding.ingredientsRecycler.asListField(
            provider = { recipeIngredients },
            validator = { list ->
                list.isNotEmpty() && list.all { it.name.isNotBlank() && it.amount.isNotBlank() } }
        )
        formValidator.addField(ingredientsField)
        val stepsField = binding.instructionsRecycler.asListField(
            provider = { recipeSteps },
            validator = { steps ->
                steps.isNotEmpty() && steps.all { it.isNotBlank() }
            }
        )
        formValidator.addField(stepsField)

        val imageField = binding.ivPhoto.asImageField { it }
        formValidator.addField(imageField)

        formValidator.isValid.observe(viewLifecycleOwner) { valid ->
            binding.publishBtn.isEnabled = valid
            binding.publishBtn.alpha = if (valid) 1f else 0.5f
        }
    }

    private fun dismiss() {
        view?.findNavController()?.popBackStack()
    }
}
