package com.example.recipease.features.view_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.databinding.FragmentViewRecipeBinding
import com.squareup.picasso.Picasso
import com.example.recipease.model.Recipe
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class ViewRecipe : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private val args: ViewRecipeArgs by navArgs()
    private lateinit var ingredientsAdapter: ViewIngredientsViewAdapter
    private lateinit var stepsAdapter: ViewStepsViewAdapter
    private lateinit var tagsAdapter: ViewTagsViewAdapter
    private lateinit var currentRecipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentRecipe = args.recipe

        // Populate recipe details
        populateRecipeDetails()

        // Set up RecyclerViews
        setupIngredientsRecycler()
        setupStepsRecycler()
        setupTagsRecycler()
    }

    private fun populateRecipeDetails() {
        binding.viewRecipeTitle.text = currentRecipe.name
        binding.viewRecipeDescription.text = currentRecipe.description
        binding.viewRecipeAuthor.text = currentRecipe.userId
        binding.viewRecipeTime.text = currentRecipe.time
        binding.viewRecipeDifficulty.text = currentRecipe.difficulty
        binding.viewRecipeServings.text = currentRecipe.servings.toString()
        binding.viewRecipeNotes.text = currentRecipe.notes
        currentRecipe.pictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .into(binding.viewRecipeImage)
            }
        }
    }

    private fun setupIngredientsRecycler() {
        ingredientsAdapter = ViewIngredientsViewAdapter(currentRecipe.ingredients)
        binding.viewRecipeIngredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ingredientsAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupStepsRecycler() {
        stepsAdapter = ViewStepsViewAdapter(currentRecipe.steps)
        binding.viewRecipeStepsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = stepsAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupTagsRecycler() {
        tagsAdapter = ViewTagsViewAdapter(currentRecipe.tags)
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