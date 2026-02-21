package com.example.recipease.features.recipes_feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.R
import com.example.recipease.databinding.FragmentRecipesFeedBinding
import com.example.recipease.model.Recipe
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class RecipesFeedFragment : Fragment() {
    private lateinit var binding: FragmentRecipesFeedBinding
    private lateinit var recipesAdapter: recipeListViewAdapter
    private val viewModel: RecipesFeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadRecipes()
        viewModel.loadTags()

        binding.tagsRecycler.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        binding.tagsRecycler.isNestedScrollingEnabled = false

        binding.RecipesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.RecipesRecycler.isNestedScrollingEnabled = false
        recipesAdapter = recipeListViewAdapter(listOf())
        binding.RecipesRecycler.adapter = recipesAdapter
        recipesAdapter.listener = object : OnRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe, position: Int) {
                onRecipeClickAction(recipe, position)
            }
        }

        observeRecipes()
        observeTags()
    }

    private fun observeRecipes() {
        viewModel.recipes.observe(viewLifecycleOwner) { list ->
            recipesAdapter.updateList(list)
        }
    }

    private fun observeTags() {
        viewModel.tags.observe(viewLifecycleOwner) { tagList ->
            binding.tagsRecycler.adapter = tagsViewAdapter(tagList) { selectedTags ->
                filterRecipes(selectedTags)
            }
        }
    }

    private fun filterRecipes(selectedTags: Set<String>) {
        val allRecipes = viewModel.recipes.value ?: emptyList()

        if (selectedTags.isEmpty()) {
            binding.titleAllRecipes.text = "All Recipes"
            recipesAdapter.updateList(allRecipes)
            return
        }

        val filtered = allRecipes.filter { recipe ->
            selectedTags.all { tag -> recipe.tags.contains(tag) }
        }

        binding.titleAllRecipes.text = "${filtered.size} Recipes"
        recipesAdapter.updateList(filtered)
    }
    private fun onRecipeClickAction(recipe: Recipe, position: Int) {
        val action = RecipesFeedFragmentDirections
            .actionRecipesFeedToViewRecipe(recipe)
        findNavController().navigate(action)
    }
}