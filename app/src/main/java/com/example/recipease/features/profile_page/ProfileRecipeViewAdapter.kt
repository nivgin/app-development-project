package com.example.recipease.features.profile_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemProfileRecipeCardBinding
import com.example.recipease.model.Recipe

interface OnProfileRecipeClickListener {
    fun onRecipeClick(recipe: Recipe, position: Int)
}

class ProfileRecipeViewAdapter(
    private var recipes: List<Recipe>,
) : RecyclerView.Adapter<ProfileRecipeViewHolder>() {
    var listener: OnProfileRecipeClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileRecipeViewHolder {
        val binding = ItemProfileRecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileRecipeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProfileRecipeViewHolder, position: Int) {
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

