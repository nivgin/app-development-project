package com.example.recipease.features.add_recipe

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.databinding.FragmentAddRecipeBinding
import com.example.recipease.features.recipes_feed.tagsViewAdapter
import com.example.recipease.model.Ingredient
import com.example.recipease.model.Recipe
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import java.util.UUID
import kotlin.getValue

class AddRecipeFragment : Fragment() {

    private lateinit var binding: FragmentAddRecipeBinding
    private lateinit var ingredientsAdapter: IngredientsViewAdapter
    private lateinit var stepsAdapter: StepsViewAdapter
    private val viewModel: AddRecipeViewModel by viewModels()
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var recipeTags: List<String> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                bitmap ->
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
        viewModel.loadTags()

        super.onViewCreated(view, savedInstanceState)

        var recipeSteps: List<String> = emptyList()
        var recipeIngredients: List<Ingredient> = emptyList()

        binding.tagsRecycler.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        binding.tagsRecycler.isNestedScrollingEnabled = false

        ingredientsAdapter = IngredientsViewAdapter(mutableListOf()) { updatedIngredients ->
            recipeIngredients = updatedIngredients.toList()
        }
        binding.ingredientsRecycler.adapter = ingredientsAdapter
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        ingredientsAdapter.addIngredient()
        binding.addIngredientBtn.setOnClickListener {
            ingredientsAdapter.addIngredient()
            binding.ingredientsRecycler.scrollToPosition(ingredientsAdapter.itemCount - 1)
        }

        stepsAdapter = StepsViewAdapter(mutableListOf()) { updatedSteps ->
            recipeSteps = updatedSteps.toList()
        }
        binding.instructionsRecycler.adapter = stepsAdapter
        binding.instructionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        stepsAdapter.addStep()
        binding.addStepBtn.setOnClickListener {
            stepsAdapter.addStep()
            binding.instructionsRecycler.scrollToPosition(stepsAdapter.itemCount - 1)
        }

        binding.publishBtn.setOnClickListener {
            val recipe = Recipe(
                id = UUID.randomUUID().toString(),
                name = binding.recipeTitle.text.toString(),
                description = binding.recipeDescription.text.toString(),
                time = binding.cookTime.text.toString(),
                difficulty = binding.difficulty.text.toString(),
                userId = "User (change when we have auth)",
                tags = recipeTags,
                steps = recipeSteps,
                ingredients = recipeIngredients,
                pictureUrl = "",
                notes = binding.notes.text.toString(),
                lastUpdated = null
            )

            binding.ivPhoto.isDrawingCacheEnabled = true
            binding.ivPhoto.buildDrawingCache()
            val imageBitmap = (binding.ivPhoto.drawable as BitmapDrawable).bitmap

            RecipeRepository.shared.addRecipe(recipe, imageBitmap) {
                dismiss()
            }
        }

        observeTags()
    }

    private fun observeTags() {
        viewModel.tags.observe(viewLifecycleOwner) { tagList ->
            binding.tagsRecycler.adapter = tagsViewAdapter(tagList) { selectedTags ->
                recipeTags = selectedTags.toList()
            }
        }
    }
    private fun dismiss() {
        view?.findNavController()?.popBackStack()
    }
}
