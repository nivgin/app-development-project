package com.example.recipease.features.add_recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemStepBinding

class StepsViewAdapter(
    private val steps: MutableList<String>,
    private val onListChanged: (List<String>) -> Unit
) : RecyclerView.Adapter<StepsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val binding = ItemStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StepsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val stepText = steps[position]

        holder.bind(
            stepText = stepText,
            stepNumber = position + 1,
            onDelete = { index ->
                steps.removeAt(index)
                notifyItemRemoved(index)
                notifyItemRangeChanged(index, steps.size)
                onListChanged(steps)
            },
            onChanged = { index, updatedText ->
                steps[index] = updatedText
                onListChanged(steps)
            }
        )
    }

    override fun getItemCount(): Int = steps.size

    fun addStep() {
        steps.add("")
        notifyItemInserted(steps.size - 1)
        onListChanged(steps)
    }
}
