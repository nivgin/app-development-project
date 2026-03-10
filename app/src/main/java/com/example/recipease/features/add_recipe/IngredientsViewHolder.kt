package com.example.recipease.features.add_recipe

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.recipease.databinding.ItemIngredientBinding
import com.example.recipease.model.FoodIdSearchResponse
import com.example.recipease.model.FoodSearchItem
import com.example.recipease.model.Ingredient
import com.example.recipease.model.ModifiedServing
import com.example.recipease.model.NutritionalContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IngredientsViewHolder(
    val binding: ItemIngredientBinding,
    private val onSearchFood: (query: String, onResults: (List<FoodSearchItem>) -> Unit) -> Unit,
    private val onGetFoodById: (foodId: String, onResult: (FoodIdSearchResponse?) -> Unit) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var searchJob: Job? = null
    private var currentIngredient: Ingredient? = null
    private var onChanged: ((Int, Ingredient) -> Unit)? = null
    private var onDelete: ((Int) -> Unit)? = null
    private var lastResults: List<FoodSearchItem> = emptyList()
    var selectedServings: List<ModifiedServing> = emptyList()
        private set

    private fun selectFood(food: FoodSearchItem) {
        onGetFoodById(food.foodId) { response ->
            selectedServings = response?.food?.servings?.serving?.mapNotNull { serving ->
                val units = serving.numberOfUnits ?: return@mapNotNull null
                if (units == 0.0) return@mapNotNull null
                val servingType = serving.measurementDescription ?: return@mapNotNull null
                ModifiedServing(
                    servingType = servingType,
                    normalizedNutritionalContent = NutritionalContent(
                        calories = serving.calories / units,
                        carbohydrate = serving.carbohydrate / units,
                        protein = serving.protein / units,
                        fat = serving.fat / units,
                        saturatedFat = serving.saturatedFat?.div(units),
                        polyunsaturatedFat = serving.polyunsaturatedFat?.div(units),
                        monounsaturatedFat = serving.monounsaturatedFat?.div(units),
                        transFat = serving.transFat?.div(units),
                        cholesterol = serving.cholesterol?.div(units),
                        sodium = serving.sodium?.div(units),
                        potassium = serving.potassium?.div(units),
                        fiber = serving.fiber?.div(units),
                        sugar = serving.sugar?.div(units),
                        addedSugars = serving.addedSugars?.div(units),
                        vitaminD = serving.vitaminD?.div(units),
                        vitaminA = serving.vitaminA?.div(units),
                        vitaminC = serving.vitaminC?.div(units),
                        calcium = serving.calcium?.div(units),
                        iron = serving.iron?.div(units)
                    )
                )
            } ?: emptyList()
            Log.i("IngredientsViewHolder", "servings: $selectedServings")
            binding.etAmount.isEnabled = selectedServings.isNotEmpty()
            servingAdapter.clear()
            servingAdapter.addAll(selectedServings.map { it.servingType })
            servingAdapter.notifyDataSetChanged()
            binding.etServing.setText("")
            binding.etServing.isEnabled = selectedServings.isNotEmpty()
        }
    }

    private val suggestionAdapter = object : ArrayAdapter<String>(
        binding.root.context,
        android.R.layout.simple_dropdown_item_1line,
        mutableListOf()
    ) {
        private val noOpFilter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?) = FilterResults()
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }
        override fun getFilter() = noOpFilter
    }

    private val servingAdapter = ArrayAdapter<String>(
        binding.root.context,
        android.R.layout.simple_dropdown_item_1line,
        mutableListOf()
    )

    init {
        binding.etIngredient.setAdapter(suggestionAdapter)
        binding.etIngredient.threshold = 2
        binding.etAmount.isEnabled = false
        binding.etServing.setAdapter(servingAdapter)
        binding.etServing.threshold = 0
        binding.etServing.isEnabled = false

        binding.etAmount.addTextChangedListener { text ->
            val ingredient = currentIngredient ?: return@addTextChangedListener
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                ingredient.amount = text?.toString().orEmpty()
                onChanged?.invoke(pos, ingredient)
            }
        }

        binding.etIngredient.addTextChangedListener { text ->
            val ingredient = currentIngredient ?: return@addTextChangedListener
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                ingredient.name = text?.toString().orEmpty()
                onChanged?.invoke(pos, ingredient)
            }

            // If the user typed manually, clear selectedServings
            if (selectedServings.isNotEmpty()) {
                selectedServings = emptyList()
                servingAdapter.clear()
                binding.etServing.setText("")
                binding.etServing.isEnabled = false
                binding.etAmount.isEnabled = false
                binding.etAmount.setText("")
            }

            val query = text?.toString() ?: return@addTextChangedListener
            if (query.length < 2) return@addTextChangedListener

            searchJob?.cancel()
            searchJob = CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                onSearchFood(query) { results ->
                    lastResults = results
                    suggestionAdapter.clear()
                    suggestionAdapter.addAll(results.map { it.foodName })
                    suggestionAdapter.notifyDataSetChanged()
                    binding.etIngredient.showDropDown()
                }
            }
        }
    }

    fun bind(
        ingredient: Ingredient,
        onDelete: (Int) -> Unit,
        onChanged: (Int, Ingredient) -> Unit
    ) {
        currentIngredient = ingredient
        this.onChanged = onChanged
        this.onDelete = onDelete

        binding.etAmount.setText(ingredient.amount)
        binding.etIngredient.setText(ingredient.name)

        // Restore amount/serving enabled state based on whether servings are already loaded
        binding.etAmount.isEnabled = selectedServings.isNotEmpty()
        binding.etServing.isEnabled = selectedServings.isNotEmpty()

        binding.etIngredient.setOnItemClickListener { _, _, position, _ ->
            val selected = suggestionAdapter.getItem(position) ?: return@setOnItemClickListener
            val food = lastResults.getOrNull(position) ?: return@setOnItemClickListener
            searchJob?.cancel()
            selectFood(food)
            ingredient.name = selected
            binding.etAmount.isEnabled = true
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onChanged(pos, ingredient)
            }
        }

        binding.etServing.setOnClickListener {
            binding.etServing.showDropDown()
        }

        binding.btnDelete.setOnClickListener {
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDelete(pos)
            }
        }
    }
}


