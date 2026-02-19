package com.example.recipease.features.view_recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemViewIngredientBinding
import com.example.recipease.model.Ingredient

class ViewIngredientsViewAdapter(
    private val ingredients: List<Ingredient>
) : RecyclerView.Adapter<ViewIngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewIngredientViewHolder {
        val binding = ItemViewIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewIngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewIngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount() = ingredients.size
}

