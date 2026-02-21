package com.example.recipease.features.recipes_feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemRecipeCardBinding
import com.example.recipease.model.Recipe

interface OnRecipeClickListener {
    fun onRecipeClick(recipe: Recipe, position: Int)
}

class recipeListViewAdapter(
    private var recipes: List<Recipe>,
) : RecyclerView.Adapter<recipeListViewHolder>() {
    var listener: OnRecipeClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recipeListViewHolder {
        val binding = ItemRecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return recipeListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: recipeListViewHolder, position: Int) {
        recipes.let { holder.bind(it[position], position) }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun updateList(newList: List<Recipe>) {
        recipes = newList
        notifyDataSetChanged()
    }
}