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

        setupChangeListener(binding.etAmount, ingredient, onChanged) { ing, text ->
            ing.copy(amount = text)
        }

        setupChangeListener(binding.etIngredient, ingredient, onChanged) { ing, text ->
            ing.copy(name = text)
        }

        binding.btnDelete.setOnClickListener {
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDelete(pos)
            }
        }
    }

    private fun setupChangeListener(
        editText: EditText,
        ingredient: Ingredient,
        onChanged: (Int, Ingredient) -> Unit,
        update: (Ingredient, String) -> Ingredient
    ) {
        editText.addTextChangedListener { editable ->
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val newText = editable?.toString().orEmpty()
                val updatedIngredient = update(ingredient, newText)
                onChanged(pos, updatedIngredient)
            }
        }
    }
}


