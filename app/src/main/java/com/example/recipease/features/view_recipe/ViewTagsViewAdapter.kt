package com.example.recipease.features.view_recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemTagBinding

class ViewTagsViewAdapter(
    private var tags: List<String>
) : RecyclerView.Adapter<ViewTagViewHolder>() {

    fun updateTags(newTags: List<String>) {
        tags = newTags
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewTagViewHolder {
        val binding = ItemTagBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewTagViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount() = tags.size
}

