package com.example.recipease.features.add_recipe

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemIngredientBinding
import com.example.recipease.model.Ingredient

class IngredientsViewHolder(
    val binding: ItemIngredientBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        ingredient: Ingredient,
        onDelete: (Int) -> Unit,
        onChanged: (Int, Ingredient) -> Unit
    ) {
        binding.etAmount.setText(ingredient.amount)
        binding.etIngredient.setText(ingredient.name)

        binding.etAmount.addTextChangedListener { text ->
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                ingredient.amount = text?.toString().orEmpty()
                onChanged(pos, ingredient)
            }
        }

        binding.etIngredient.addTextChangedListener { text ->
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                ingredient.name = text?.toString().orEmpty()
                onChanged(pos, ingredient)
            }
        }

        binding.btnDelete.setOnClickListener {
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDelete(pos)
            }
        }
    }
}


