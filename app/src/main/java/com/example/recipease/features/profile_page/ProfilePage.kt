package com.example.recipease.features.profile_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipease.data.models.FirebaseAuthModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.recipease.databinding.FragmentProfilePageBinding
import com.example.recipease.model.Recipe
import com.example.recipease.data.repository.UserRepository
import com.squareup.picasso.Picasso
import android.graphics.drawable.BitmapDrawable

class ProfilePage : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var profileRecipeAdapter: ProfileRecipeViewAdapter
    private val viewModel: ProfilePageViewModel by viewModels()
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null


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

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                binding.editProfileImagePreview.setImageBitmap(it)
                binding.editProfilePhotoPlaceholder.visibility = View.GONE
            } ?: Toast.makeText(context, "No image captured", Toast.LENGTH_SHORT).show()
        }

        binding.editProfilePhotoPicker.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        binding.editProfileBtn.setOnClickListener {
            enterEditMode()
        }

        binding.cancelProfileBtn.setOnClickListener {
            exitEditMode()
        }

        binding.saveProfileBtn.setOnClickListener {
            val newName = binding.editNameInput.text.toString().trim()
            val currentUser = viewModel.connectedUser.value ?: return@setOnClickListener

            val imageBitmap = (binding.editProfileImagePreview.drawable as BitmapDrawable).bitmap

            val updatedUser = currentUser.copy(displayName = newName)
            UserRepository.shared.addUser(updatedUser, imageBitmap) {
                viewModel.refreshUser()
            }
            exitEditMode()
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

    private fun enterEditMode() {
        binding.editNameInput.setText(binding.profileName.text)
        binding.editProfilePhotoPlaceholder.visibility = View.VISIBLE
        viewModel.connectedUser.value?.profilePictureUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get().load(it).into(binding.editProfileImagePreview)
            }
        }
        binding.profileCard.visibility = View.GONE
        binding.editProfileCard.visibility = View.VISIBLE
    }

    private fun exitEditMode() {
        binding.editProfileCard.visibility = View.GONE
        binding.profileCard.visibility = View.VISIBLE
    }

    private fun setupRecipeRecyclerView() {
        profileRecipeAdapter = ProfileRecipeViewAdapter(viewModel.userRecipes.value ?: emptyList())

        profileRecipeAdapter.listener = object : OnProfileRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe, position: Int) {
                val action = ProfilePageDirections.actionProfilePageToManageRecipe(
                    recipeId = recipe.id,
                    userId = viewModel.connectedUser.value?.id
                )
                findNavController().navigate(action)
            }
        }

        binding.myRecipesRecycler.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = profileRecipeAdapter
        }
    }
}