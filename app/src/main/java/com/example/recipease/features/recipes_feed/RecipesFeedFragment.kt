package com.example.recipease.features.recipes_feed

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.databinding.FragmentRecipesFeedBinding
import com.example.recipease.model.Recipe
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.example.recipease.features.recipes_feed.RecipesFeedViewModel.RecipeWithUser

class RecipesFeedFragment : Fragment() {
    private lateinit var binding: FragmentRecipesFeedBinding
    private lateinit var recipesAdapter: recipeListViewAdapter
    private lateinit var tagsAdapter: tagsViewAdapter
    private val viewModel: RecipesFeedViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.refreshRecipes()
        viewModel.refreshTags()
        viewModel.refreshUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTags()
        setupRecipes()
        binding.searchBar.addTextChangedListener { editable ->
            val searchText = editable?.toString() ?: ""
            viewModel.processRecipes(viewModel.currentSelectedTags, searchText)
        }
        observeLoading()
        observeRecipes()
        observeTags()
        observeUsername()
    }

    private fun observeRecipes() {
        viewModel.displayedRecipes.observe(viewLifecycleOwner) { list ->
            recipesAdapter.updateList(list)
            binding.titleAllRecipes.text = if (viewModel.currentSelectedTags.isNotEmpty() || viewModel.currentSearchFilter.isNotEmpty()) {
                "${list.size} Recipes"
            } else {
                "All Recipes"
            }
        }
    }

    private fun observeTags() {
        viewModel.tags.observe(viewLifecycleOwner) { tagList ->
            tagsAdapter.updateList(tagList)
            tagsAdapter.updateSelected(viewModel.currentSelectedTags)
        }
    }

    private fun observeUsername() {
        viewModel.connectedUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.greetUser.text = "Hello, ${user.displayName} \uD83D\uDC4B"
            }
        }
    }

    private fun observeLoading() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshRecipes()
            viewModel.refreshTags()
            viewModel.refreshUsers()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.loadingIndicator.visibility = if (loading) View.VISIBLE else View.GONE
            binding.swipeRefresh.isRefreshing = loading
        }
    }

    private fun setupTags() {
        binding.tagsRecycler.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        binding.tagsRecycler.isNestedScrollingEnabled = false
        tagsAdapter = tagsViewAdapter(emptyList()) { selectedTags ->
            val searchText = binding.searchBar.text.toString()
            viewModel.processRecipes(selectedTags, searchText)
        }
        binding.tagsRecycler.adapter = tagsAdapter
    }

    private fun setupRecipes() {
        binding.RecipesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.RecipesRecycler.isNestedScrollingEnabled = false
        recipesAdapter = recipeListViewAdapter(emptyList())
        binding.RecipesRecycler.adapter = recipesAdapter
        recipesAdapter.listener = object : OnRecipeClickListener {
            override fun onRecipeClick(recipeWithUser: RecipeWithUser, position: Int) {
                onRecipeClickAction(recipeWithUser, position)
            }
        }
    }

    private fun onRecipeClickAction(recipeWithUser: RecipeWithUser, position: Int) {
        val action = RecipesFeedFragmentDirections
            .actionRecipesFeedToViewRecipe(recipeWithUser.recipe.id, recipeWithUser.user?.id)

        findNavController().navigate(action)
    }
}