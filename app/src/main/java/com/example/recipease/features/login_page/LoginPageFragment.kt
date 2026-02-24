package com.example.recipease.features.login_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipease.R
import com.example.recipease.databinding.FragmentLoginPageBinding


class LoginPageFragment : Fragment() {
    private lateinit var binding: FragmentLoginPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginPageBinding.inflate(inflater, container, false)

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
    }

    fun changeSignupVisibility(state: Int) {
        binding.fullNameLabel.visibility = state
        binding.fullNameField.visibility = state
        binding.photoPicker.visibility = state
        binding.pfpLabel.visibility = state
    }
}
