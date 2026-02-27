package com.example.recipease.features.login_page

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.recipease.data.models.FirebaseAuthModel
import com.example.recipease.databinding.FragmentLoginPageBinding

class LoginPageFragment : Fragment() {

    private lateinit var binding: FragmentLoginPageBinding
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginPageBinding.inflate(inflater, container, false)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                binding.ivPhoto.setImageBitmap(it)
                binding.ivCamera.visibility = View.GONE
                binding.tvAddPhoto.visibility = View.GONE
            } ?: Toast.makeText(context, "No image captured", Toast.LENGTH_SHORT).show()
        }

        binding.photoPicker.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabSignIn.isSelected = true
        changeSignupVisibility(View.GONE)

        binding.tabSignIn.setOnClickListener {
            binding.tabSignIn.isSelected = true
            binding.tabSignUp.isSelected = false
            binding.btnSignIn.text = "Sign In"
            changeSignupVisibility(View.GONE)
        }

        binding.tabSignUp.setOnClickListener {
            binding.tabSignIn.isSelected = false
            binding.tabSignUp.isSelected = true
            binding.btnSignIn.text = "Sign Up"
            changeSignupVisibility(View.VISIBLE)
        }

        binding.btnSignIn.setOnClickListener {
            if (binding.tabSignIn.isSelected) {
                handleSignIn()
            } else {
                handleSignUp()
            }
        }

        if (FirebaseAuthModel.shared.isLoggedIn()) {
            navigateToHome()
        }
    }

    private fun handleSignIn() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuthModel.shared.signIn(email, password) {
            if (FirebaseAuthModel.shared.isLoggedIn()) {
                val id = FirebaseAuthModel.shared.getCurrentUserId()
                if (id != null) {
                    FirebaseAuthModel.shared.populateUser(id)
                }
                navigateToHome()
            } else {
                Toast.makeText(context, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignUp() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        val fullname = binding.fullNameInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.ivPhoto.isDrawingCacheEnabled = true
        binding.ivPhoto.buildDrawingCache()
        val imageBitmap = (binding.ivPhoto.drawable as BitmapDrawable).bitmap

        FirebaseAuthModel.shared.signUp(email, password, fullname, imageBitmap) {
            if (FirebaseAuthModel.shared.isLoggedIn()) {
                navigateToHome()
            } else {
                Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHome() {
        view?.findNavController()?.navigate(
            LoginPageFragmentDirections.actionLoginPageToRecipesFeed()
        )
    }

    fun changeSignupVisibility(state: Int) {
        binding.fullNameLabel.visibility = state
        binding.fullNameField.visibility = state
        binding.photoPicker.visibility = state
        binding.pfpLabel.visibility = state
    }
}