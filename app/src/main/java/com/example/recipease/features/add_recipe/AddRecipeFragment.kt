package com.example.recipease.features.add_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.R
import com.example.recipease.databinding.FragmentAddRecipeBinding
import com.example.recipease.features.recipes_feed.tagsViewAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

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

        val tags = listOf(
            "Burgers", "Italian", "Dessert", "Vegan", "Asian",
            "BBQ", "Mexican", "Healthy", "Pasta", "Salads"
        )

        binding.tagsRecycler.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        binding.tagsRecycler.isNestedScrollingEnabled = false
        binding.tagsRecycler.adapter = tagsViewAdapter(tags) { selectedTags ->
            // Use it here
        }

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
