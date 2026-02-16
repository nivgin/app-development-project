package com.example.recipease.features.add_recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemIngredientBinding
import com.example.recipease.model.Ingredient

class IngredientsViewAdapter(
    private val ingredients: MutableList<Ingredient>,
    private val onListChanged: (List<Ingredient>) -> Unit
) : RecyclerView.Adapter<IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]

        holder.bind(
            ingredient,
            onDelete = { index ->
                ingredients.removeAt(index)
                notifyItemRemoved(index)
                onListChanged(ingredients)
            },
            onChanged = { index, updated ->
                ingredients[index] = updated
                onListChanged(ingredients)
            }
        )
    }

    override fun getItemCount(): Int = ingredients.size

    fun addIngredient() {
        ingredients.add(Ingredient())
        notifyItemInserted(ingredients.size - 1)
        onListChanged(ingredients)
    }
}
