package com.example.recipease.features.profile_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipease.data.models.FirebaseAuthModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.example.recipease.databinding.FragmentProfilePageBinding
import com.example.recipease.model.Recipe

class ProfilePage : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var profileRecipeAdapter: ProfileRecipeViewAdapter

    private val mockRecipes = listOf(
        Recipe("Chocolate Cake", "", "", "", "", 0, "", emptyList(), emptyList(), emptyList(), pictureUrl = "", notes = "", lastUpdated = 0L),
        Recipe("Pasta Carbonara", "", "", "", "", 0, "", emptyList(), emptyList(), emptyList(), pictureUrl = "", notes = "", lastUpdated = 0L),
        Recipe("Greek Salad", "", "", "", "", 0, "", emptyList(), emptyList(), emptyList(), pictureUrl = "", notes = "", lastUpdated = 0L),
        Recipe("Chicken Tikka", "", "", "", "", 0, "", emptyList(), emptyList(), emptyList(), pictureUrl = "", notes = "", lastUpdated = 0L),
        Recipe("Beef Tacos", "", "", "", "", 0, "", emptyList(), emptyList(), emptyList(), pictureUrl = "", notes = "", lastUpdated = 0L),
        Recipe("Sushi Rolls", "", "", "", "", 0, "", emptyList(), emptyList(), emptyList(), pictureUrl = "", notes = "", lastUpdated = 0L),
        Recipe("Banana Bread", "", "", "", "", 0, "", emptyList(), emptyList(), emptyList(), pictureUrl = "", notes = "", lastUpdated = 0L)
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

        binding.editProfileBtn.setOnClickListener {
            FirebaseAuthModel.shared.signOut()
        }

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