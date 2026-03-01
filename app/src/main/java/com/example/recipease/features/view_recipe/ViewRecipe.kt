package com.example.recipease.features.view_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.databinding.FragmentViewRecipeBinding
import com.squareup.picasso.Picasso
import com.example.recipease.model.Recipe
import androidx.navigation.fragment.navArgs
import com.example.recipease.model.User
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class ViewRecipe : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private val args: ViewRecipeArgs by navArgs()
    private val viewModel: ViewRecipeViewModel by viewModels()

    private lateinit var ingredientsAdapter: ViewIngredientsViewAdapter
    private lateinit var stepsAdapter: ViewStepsViewAdapter
    private lateinit var tagsAdapter: ViewTagsViewAdapter

    override fun onResume() {
        super.onResume()
        viewModel.refreshRecipes()
        viewModel.refreshUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupIngredientsRecycler()
        setupStepsRecycler()
        setupTagsRecycler()

        viewModel.init(args.recipeId, args.userId)

        observeRecipe()
        observeUser()
    }

    private fun observeRecipe() {
        viewModel.currentRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe ?: return@observe
            populateRecipeDetails(recipe)
        }
    }

    private fun observeUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            populateUserDetails(user)
        }
    }

    private fun populateRecipeDetails(recipe: Recipe) {
        binding.viewRecipeTitle.text = recipe.name
        binding.viewRecipeDescription.text = recipe.description
        binding.viewRecipeTime.text = recipe.time
        binding.viewRecipeDifficulty.text = recipe.difficulty
        binding.viewRecipeServings.text = recipe.servings.toString()
        binding.viewRecipeNotes.text = recipe.notes
        recipe.pictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get().load(it).into(binding.viewRecipeImage)
            }
        }
        ingredientsAdapter.updateIngredients(recipe.ingredients)
        stepsAdapter.updateSteps(recipe.steps)
        tagsAdapter.updateTags(recipe.tags)
    }

    private fun populateUserDetails(user: User?) {
        val displayUser = user ?: User(id = "", displayName = "Unknown", profilePictureUrl = null, lastUpdated = null)
        binding.viewRecipeAuthor.text = displayUser.displayName
        displayUser.profilePictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get().load(it).into(binding.viewRecipeAuthorImage)
            }
        }
    }

    private fun setupIngredientsRecycler() {
        ingredientsAdapter = ViewIngredientsViewAdapter(emptyList())
        binding.viewRecipeIngredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ingredientsAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupStepsRecycler() {
        stepsAdapter = ViewStepsViewAdapter(emptyList())
        binding.viewRecipeStepsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = stepsAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupTagsRecycler() {
        tagsAdapter = ViewTagsViewAdapter(emptyList())
        binding.viewRecipeTagsRecyclerView.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = tagsAdapter
            isNestedScrollingEnabled = false
        }
    }
}