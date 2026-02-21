package com.example.recipease.features.recipes_feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import androidx.fragment.app.viewModels
=======
import android.widget.TextView
>>>>>>> b81315cb04573b0502244434b2e76f1d99980312
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.R
<<<<<<< HEAD
import com.example.recipease.databinding.FragmentRecipesFeedBinding
=======
import com.example.recipease.model.Ingredient
>>>>>>> b81315cb04573b0502244434b2e76f1d99980312
import com.example.recipease.model.Recipe
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class RecipesFeedFragment : Fragment() {
    private lateinit var binding: FragmentRecipesFeedBinding
    private lateinit var recipesAdapter: recipeListViewAdapter
<<<<<<< HEAD
    private val viewModel: RecipesFeedViewModel by viewModels()
=======
    val tags = listOf(
        "Burgers", "Italian", "Dessert", "Vegan", "Asian",
        "BBQ", "Mexican", "Healthy", "Pasta", "Salads"
    )

    val recipes = listOf(
        Recipe(
            name = "Creamy Garlic Pasta",
            description = "A rich and silky pasta tossed in a velvety garlic‑parmesan sauce.",
            time = "25 min",
            difficulty = "Easy",
            servings = "4",
            notes = "Don't make the sauce too thick!",
            author = "Sofia Bennett",
            tags = listOf("Italian", "Pasta"),
            ingredients = listOf(),
            steps = listOf()
        ),
        Recipe(
            name = "Spicy Honey Chicken Bowl",
            description = "Crispy chicken glazed with spicy honey, served over warm jasmine rice.",
            time = "40 min",
            difficulty = "Medium",
            author = "Marcus Lee",
            servings = "4",
            notes = "",
            tags = listOf("Asian", "Healthy"),
            ingredients = listOf(),
            steps = listOf()
        ),
        Recipe(
            name = "Classic Chocolate Lava Cake",
            description = "A decadent dessert with a molten chocolate center that melts in your mouth.",
            time = "30 min",
            difficulty = "Hard",
            author = "Elena Rivera",
            servings = "4",
            notes = "",
            tags = listOf("Dessert", "Healthy"),
            ingredients = listOf(),
            steps = listOf()
        )
    )
>>>>>>> b81315cb04573b0502244434b2e76f1d99980312

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

<<<<<<< HEAD
        binding.RecipesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.RecipesRecycler.isNestedScrollingEnabled = false
        recipesAdapter = recipeListViewAdapter(listOf())
        binding.RecipesRecycler.adapter = recipesAdapter
=======
        val recipesRecycler = view.findViewById<RecyclerView>(R.id.RecipesRecycler)
        recipesRecycler.layoutManager = LinearLayoutManager(requireContext())
        recipesRecycler.isNestedScrollingEnabled = false
        recipesAdapter = recipeListViewAdapter(recipes)
>>>>>>> b81315cb04573b0502244434b2e76f1d99980312
        recipesAdapter.listener = object : OnRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe, position: Int) {
                onRecipeClickAction(recipe, position)
            }
        }
<<<<<<< HEAD

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
=======
        recipesRecycler.adapter = recipesAdapter
>>>>>>> b81315cb04573b0502244434b2e76f1d99980312
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
<<<<<<< HEAD
=======

>>>>>>> b81315cb04573b0502244434b2e76f1d99980312
    private fun onRecipeClickAction(recipe: Recipe, position: Int) {
        findNavController().navigate(R.id.action_RecipesFeed_to_ViewRecipe)
    }
}