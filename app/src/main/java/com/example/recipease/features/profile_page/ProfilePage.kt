package com.example.recipease.features.profile_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipease.data.models.FirebaseAuthModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.recipease.databinding.FragmentProfilePageBinding
import com.example.recipease.model.Recipe
import com.squareup.picasso.Picasso

class ProfilePage : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var profileRecipeAdapter: ProfileRecipeViewAdapter
    private val viewModel: ProfilePageViewModel by viewModels()


    override fun onResume() {
        super.onResume()
        viewModel.refreshRecipes()
        viewModel.refreshUser()
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeRecipes()
        observeConnectedUser()
    }

    private fun observeConnectedUser() {
        viewModel.connectedUser.observe(viewLifecycleOwner) { user ->
            binding.profileName.text = user?.displayName
            user?.profilePictureUrl?.let {
                if (it.isNotBlank()) {
                    Picasso.get()
                        .load(it)
                        .into(binding.profileImage)
                }
            }
        }
    }

    private fun observeRecipes() {
        viewModel.userRecipes.observe(viewLifecycleOwner) { recipes ->
            profileRecipeAdapter.updateList(recipes)
        }
    }

    private fun setupRecipeRecyclerView() {
        profileRecipeAdapter = ProfileRecipeViewAdapter(viewModel.userRecipes.value ?: emptyList())

        // Set up click listener
        profileRecipeAdapter.listener = object : OnProfileRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe, position: Int) {
                val action = ProfilePageDirections.actionProfilePageToManageRecipe(
                    recipe = recipe,
                    user = viewModel.connectedUser.value
                )
                findNavController().navigate(action)
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