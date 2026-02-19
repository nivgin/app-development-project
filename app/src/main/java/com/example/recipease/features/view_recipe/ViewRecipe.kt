package com.example.recipease.features.view_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.databinding.FragmentViewRecipeBinding
import com.example.recipease.model.Ingredient
import com.example.recipease.model.Recipe

class ViewRecipe : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private lateinit var ingredientsAdapter: ViewIngredientsViewAdapter
    private lateinit var stepsAdapter: ViewStepsViewAdapter
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
        currentRecipe = Recipe(
            name = "Creamy Garlic Pasta",
            description = "A rich and silky pasta tossed in a velvety garlic‑parmesan sauce.",
            time = "25 min",
            difficulty = "Easy",
            servings = "4",
            notes = "Don't make the sauce too thick!",
            author = "Sofia Bennett",
            tags = listOf("Italian", "Pasta"),
            ingredients = listOf(
                Ingredient("400g", "Pasta"),
                Ingredient("4 cloves", "Garlic, minced"),
                Ingredient("200ml", "Heavy cream"),
                Ingredient("100g", "Parmesan cheese"),
                Ingredient("2 tbsp", "Olive oil"),
                Ingredient("To taste", "Salt and pepper")
            ),
            steps = listOf(
                "Bring a large pot of salted water to boil and cook pasta according to package instructions.",
                "While pasta cooks, heat olive oil in a large pan over medium heat. Add minced garlic and sauté until fragrant, about 1 minute.",
                "Pour in heavy cream and bring to a gentle simmer. Let it cook for 2-3 minutes until slightly thickened.",
                "Add grated parmesan cheese and stir until melted and smooth.",
                "Drain pasta and add to the sauce. Toss well to coat evenly.",
                "Season with salt and pepper to taste. Serve immediately with extra parmesan on top."
            )
        )

        // Populate recipe details
        populateRecipeDetails()

        // Set up RecyclerViews
        setupIngredientsRecycler()
        setupStepsRecycler()
    }

    private fun populateRecipeDetails() {
        binding.viewRecipeTitle.text = currentRecipe.name
        binding.viewRecipeDescription.text = currentRecipe.description
        binding.viewRecipeAuthor.text = currentRecipe.author
        binding.viewRecipeTime.text = currentRecipe.time
        binding.viewRecipeDifficulty.text = currentRecipe.difficulty
        binding.viewRecipeServings.text = currentRecipe.servings
        binding.viewRecipeNotes.text = currentRecipe.notes
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
}