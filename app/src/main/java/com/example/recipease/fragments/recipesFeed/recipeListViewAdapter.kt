package com.example.recipease.fragments.recipesFeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemRecipeCardBinding
import com.example.recipease.model.Recipe

class recipeListViewAdapter(private var recipes: List<Recipe>) : RecyclerView.Adapter<recipeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recipeListViewHolder {
        val binding = ItemRecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return recipeListViewHolder(binding)
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