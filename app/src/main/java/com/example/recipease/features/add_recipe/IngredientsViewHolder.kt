package com.example.recipease.features.add_recipe

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private var suppressTextClear = false
    var selectedServings: List<ModifiedServing> = emptyList()
        private set

    private val servingSpinnerAdapter = ArrayAdapter<String>(
        binding.root.context,
        android.R.layout.simple_spinner_item,
        mutableListOf()
    ).also {
        it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

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
            Log.i("TEST", "servings: $selectedServings")
            servingSpinnerAdapter.clear()
            servingSpinnerAdapter.addAll(selectedServings.map { it.servingType })
            servingSpinnerAdapter.notifyDataSetChanged()
            binding.etServing.isEnabled = selectedServings.isNotEmpty()
            binding.etAmount.isEnabled = selectedServings.isNotEmpty()
            currentIngredient?.food = food
            currentIngredient?.serving = null
        }
    }

    private fun notifyChanged() {
        val ingredient = currentIngredient ?: return
        val pos = bindingAdapterPosition
        if (pos != RecyclerView.NO_POSITION) onChanged?.invoke(pos, ingredient)
    }

    fun setServing(serving: ModifiedServing) {
        selectedServings = listOf(serving)
        servingSpinnerAdapter.clear()
        servingSpinnerAdapter.add(serving.servingType)
        servingSpinnerAdapter.notifyDataSetChanged()
        binding.etServing.setSelection(0)
        binding.etServing.isEnabled = true
        currentIngredient?.serving = serving
    }

    init {
        binding.etIngredient.threshold = 2
        binding.etAmount.isEnabled = false
        binding.etServing.adapter = servingSpinnerAdapter
        binding.etServing.isEnabled = false

        // These listeners must be in init instead of bind. because these functions add instead of
        // setting, if they were in bind, we could get multiple listeners at the same time.
        binding.etAmount.addTextChangedListener { text ->
            val ingredient = currentIngredient ?: return@addTextChangedListener
            ingredient.amount = text?.toString()?.toDoubleOrNull() ?: 0.0
            notifyChanged()
        }

        binding.etIngredient.addTextChangedListener { text ->
            val ingredient = currentIngredient ?: return@addTextChangedListener

            // If the user typed manually (not from a food selection), clear food/serving
            if (!suppressTextClear && selectedServings.isNotEmpty()) {
                selectedServings = emptyList()
                ingredient.food = null
                ingredient.serving = null
                servingSpinnerAdapter.clear()
                servingSpinnerAdapter.notifyDataSetChanged()
                binding.etServing.isEnabled = false
                binding.etAmount.isEnabled = false
                binding.etAmount.setText("")
            }

            val query = text?.toString() ?: return@addTextChangedListener
            if (query.length < 2) return@addTextChangedListener

            searchJob?.cancel()
            if (!suppressTextClear) {
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    Log.i("TEST", "Searching for: $query")
                    onSearchFood(query) { results ->
                        Log.i("TEST", "Completed search for $query $results")
                        lastResults = results
                        binding.etIngredient.setSimpleItems(results.map { it.foodName }.toTypedArray())
                        binding.etIngredient.showDropDown()
                    }
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

        suppressTextClear = true

        // Populate fields from ingredient
        binding.etIngredient.setText(ingredient.food?.foodName ?: "", false)
        binding.etAmount.setText(ingredient.amount.toString())
        if (selectedServings.isEmpty()) {
            ingredient.serving?.let {
                setServing(it)
            }
        }

        // Restore amount/serving enabled state based on whether servings are already loaded
        binding.etAmount.isEnabled = selectedServings.isNotEmpty()
        binding.etServing.isEnabled = selectedServings.isNotEmpty()

        binding.etIngredient.setOnItemClickListener { _, _, position, _ ->
            val selected = lastResults.getOrNull(position) ?: return@setOnItemClickListener
            searchJob?.cancel()
            suppressTextClear = true
            selectFood(selected)
            binding.etIngredient.setText(selected.foodName, false)
            suppressTextClear = false
            notifyChanged()
        }

        binding.etServing.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val serving = selectedServings.getOrNull(position) ?: return
                ingredient.serving = serving
                notifyChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnDelete.setOnClickListener {
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) onDelete.invoke(pos)
        }

        suppressTextClear = false
    }
}
