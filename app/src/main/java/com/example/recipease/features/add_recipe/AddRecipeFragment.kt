package com.example.recipease.features.add_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipease.databinding.FragmentAddRecipeBinding

class AddRecipeFragment : Fragment() {

    private lateinit var binding: FragmentAddRecipeBinding
    private lateinit var ingredientsAdapter: IngredientsViewAdapter
    private lateinit var stepsAdapter: StepsViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingredientsAdapter = IngredientsViewAdapter(mutableListOf()) { updatedList ->
            // You get the updated list here if you need it
        }
        binding.ingredientsRecycler.adapter = ingredientsAdapter
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        ingredientsAdapter.addIngredient()
        binding.addIngredientBtn.setOnClickListener {
            ingredientsAdapter.addIngredient()
            binding.ingredientsRecycler.scrollToPosition(ingredientsAdapter.itemCount - 1)
        }

        stepsAdapter = StepsViewAdapter(mutableListOf()) { updatedList ->
            // You get the updated list here if you need it
        }
        binding.instructionsRecycler.adapter = stepsAdapter
        binding.instructionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        stepsAdapter.addStep()
        binding.addStepBtn.setOnClickListener {
            stepsAdapter.addStep()
            binding.instructionsRecycler.scrollToPosition(stepsAdapter.itemCount - 1)
        }
    }
}
