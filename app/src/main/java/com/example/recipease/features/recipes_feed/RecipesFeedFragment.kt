package com.example.recipease.features.recipes_feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.R
import com.example.recipease.model.Recipe
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class RecipesFeedFragment : Fragment() {
    private lateinit var recipesAdapter: recipeListViewAdapter
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
            tags = listOf("Dessert", "Healthy"),
            ingredients = listOf(),
            steps = listOf()
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tagsRecycler = view.findViewById<RecyclerView>(R.id.tagsRecycler)
        tagsRecycler.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        tagsRecycler.isNestedScrollingEnabled = false
        tagsRecycler.adapter = tagsViewAdapter(tags) { selectedTags -> filterRecipes(selectedTags) }

        val recipesRecycler = view.findViewById<RecyclerView>(R.id.RecipesRecycler)
        recipesRecycler.layoutManager = LinearLayoutManager(requireContext())
        recipesRecycler.isNestedScrollingEnabled = false
        recipesAdapter = recipeListViewAdapter(recipes)
        recipesAdapter.listener = object : OnRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe, position: Int) {
                onRecipeClickAction(recipe, position)
            }
        }
        recipesRecycler.adapter = recipesAdapter
    }

    private fun filterRecipes(selectedTags: Set<String>) {
        val recipesCountView = view?.findViewById<TextView>(R.id.titleAllRecipes)

        if (selectedTags.isEmpty()) {
            recipesCountView?.text = "All Recipes"
            recipesAdapter.updateList(recipes)
            return
        }

        val filtered = recipes.filter { recipe ->
            selectedTags.all { tag -> recipe.tags.contains(tag) }
        }

        recipesCountView?.text = "${filtered.size} Recipes"
        recipesAdapter.updateList(filtered)
    }

    private fun onRecipeClickAction(recipe: Recipe, position: Int) {
        findNavController().navigate(R.id.action_RecipesFeed_to_ViewRecipe)
    }
}