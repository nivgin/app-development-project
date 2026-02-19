package com.example.recipease.features.view_recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemViewStepBinding

class ViewStepsViewAdapter(
    private val steps: List<String>
) : RecyclerView.Adapter<ViewStepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewStepViewHolder {
        val binding = ItemViewStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewStepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewStepViewHolder, position: Int) {
        holder.bind(steps[position], position)
    }

    override fun getItemCount() = steps.size
}

