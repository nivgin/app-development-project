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
import com.example.recipease.base.Constants
import com.example.recipease.data.models.FirebaseAuthModel
import com.example.recipease.databinding.FragmentLoginPageBinding
import com.example.recipease.utils.FormValidator
import com.example.recipease.utils.asFormField
import com.example.recipease.utils.asImageField

class LoginPageFragment : Fragment() {

    private lateinit var binding: FragmentLoginPageBinding

    private val signInValidator = FormValidator()
    private val signUpValidator = FormValidator()
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
                signUpValidator.update()
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

        binding.loadingIndicator.visibility = View.GONE

        binding.tabSignIn.setOnClickListener {
            switchPageMode(true)
        }

        binding.tabSignUp.setOnClickListener {
            switchPageMode(false)
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

        validateForm()
        observeValidators()
    }

    private fun handleSignIn() {
        binding.loadingIndicator.visibility = View.VISIBLE
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

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
            binding.loadingIndicator.visibility = View.GONE
        }
    }

    private fun handleSignUp() {
        binding.loadingIndicator.visibility = View.VISIBLE
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        val fullname = binding.fullNameInput.text.toString().trim()

        binding.ivPhoto.isDrawingCacheEnabled = true
        binding.ivPhoto.buildDrawingCache()
        val imageBitmap = (binding.ivPhoto.drawable as BitmapDrawable).bitmap

        FirebaseAuthModel.shared.signUp(email, password, fullname, imageBitmap) {
            if (FirebaseAuthModel.shared.isLoggedIn()) {
                navigateToHome()
            } else {
                Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
            }
            binding.loadingIndicator.visibility = View.GONE
        }
    }

    private fun navigateToHome() {
        view?.findNavController()?.navigate(
            LoginPageFragmentDirections.actionLoginPageToRecipesFeed()
        )
    }

    private fun validateForm() {

        signInValidator.addField(
            binding.emailInput.asFormField { it.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }
        )
        signInValidator.addField(
            binding.passwordInput.asFormField { it.length >= Constants.MIN_PASSWORD_LEN }
        )
        signUpValidator.addField(
            binding.emailInput.asFormField { it.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }
        )
        signUpValidator.addField(
            binding.passwordInput.asFormField { it.length >= Constants.MIN_PASSWORD_LEN }
        )
        signUpValidator.addField(
            binding.fullNameInput.asFormField { it.length >= Constants.MIN_NAME_LEN }
        )
        signUpValidator.addField(
            binding.ivPhoto.asImageField { it }
        )
    }

    private fun observeValidators() {

        signInValidator.isValid.observe(viewLifecycleOwner) { valid ->
            if (binding.tabSignIn.isSelected) {
                binding.btnSignIn.isEnabled = valid
                binding.btnSignIn.alpha = if (valid) 1f else 0.5f
            }
        }

        signUpValidator.isValid.observe(viewLifecycleOwner) { valid ->
            if (binding.tabSignUp.isSelected) {
                binding.btnSignIn.isEnabled = valid
                binding.btnSignIn.alpha = if (valid) 1f else 0.5f
            }
        }
    }

    private fun switchPageMode(signIn: Boolean) {
        binding.tabSignIn.isSelected = signIn
        binding.tabSignUp.isSelected = !signIn
        binding.btnSignIn.text = if (signIn) "Sign In" else "Sign Up"
        changeSignupVisibility(if (signIn) View.GONE else View.VISIBLE)
        signUpValidator.update()
    }

    fun changeSignupVisibility(state: Int) {
        binding.fullNameLabel.visibility = state
        binding.fullNameField.visibility = state
        binding.photoPicker.visibility = state
        binding.pfpLabel.visibility = state
    }
}