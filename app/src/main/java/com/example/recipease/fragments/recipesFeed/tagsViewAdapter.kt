package com.example.recipease.fragments.recipesFeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemTagBinding

class tagsViewAdapter(
    private val tags: List<String>,
    private val onSelectionChanged: (Set<String>) -> Unit
) : RecyclerView.Adapter<tagsViewHolder>() {

    private val selected = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): tagsViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return tagsViewHolder(
            binding,
            selected,
            notifyChange = { pos ->
                notifyItemChanged(pos)
                onSelectionChanged(selected.map { tags[it] }.toSet())
            }
        )
    }

    override fun onBindViewHolder(holder: tagsViewHolder, position: Int) {
        holder.bind(tags[position], position)
    }

    override fun getItemCount(): Int = tags.size
}


