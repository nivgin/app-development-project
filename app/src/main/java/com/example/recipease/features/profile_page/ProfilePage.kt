package com.example.recipease.features.profile_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.example.recipease.databinding.FragmentProfilePageBinding
import com.example.recipease.model.Recipe

class ProfilePage : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var profileRecipeAdapter: ProfileRecipeViewAdapter

    private val mockRecipes = listOf(
        Recipe("Chocolate Cake", "", "", "", "", "", "", emptyList(), emptyList(), emptyList()),
        Recipe("Pasta Carbonara", "", "", "", "", "", "", emptyList(), emptyList(), emptyList()),
        Recipe("Greek Salad", "", "", "", "", "", "", emptyList(), emptyList(), emptyList()),
        Recipe("Chicken Tikka", "", "", "", "", "", "", emptyList(), emptyList(), emptyList()),
        Recipe("Beef Tacos", "", "", "", "", "", "", emptyList(), emptyList(), emptyList()),
        Recipe("Sushi Rolls", "", "", "", "", "", "", emptyList(), emptyList(), emptyList()),
        Recipe("Banana Bread", "", "", "", "", "", "", emptyList(), emptyList(), emptyList())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        setupRecipeRecyclerView()
        return binding.root
    }

    private fun setupRecipeRecyclerView() {
        // Initialize adapter with mock recipes for testing
        profileRecipeAdapter = ProfileRecipeViewAdapter(mockRecipes)

        // Set up click listener
        profileRecipeAdapter.listener = object : OnProfileRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe, position: Int) {
                // TODO: Navigate to manage recipe
            }
        }

        // Set up RecyclerView with FlexboxLayoutManager for flexible wrapping
        binding.myRecipesRecycler.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = profileRecipeAdapter
        }
    }
}