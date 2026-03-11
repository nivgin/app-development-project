package com.example.recipease.features.view_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recipease.data.repository.RecipeRepository
import com.example.recipease.data.repository.UserRepository
import com.example.recipease.model.NutritionalContent
import com.example.recipease.model.Recipe
import com.example.recipease.model.User

class ViewRecipeViewModel : ViewModel() {

    private val recipeRepo = RecipeRepository.shared
    private val userRepo = UserRepository.shared

    lateinit var currentRecipe: LiveData<Recipe>
        private set

    lateinit var currentUser: LiveData<User?>
        private set

    fun init(recipeId: String, userId: String?) {
        currentRecipe = recipeRepo.getRecipeById(recipeId)
        currentUser = if (!userId.isNullOrBlank()) {
            userRepo.getUserById(userId)
        } else {
            userRepo.getUserById("")
        }
    }

    fun calculateNutritionalText(recipe: Recipe): String {
        val totals = recipe.ingredients.fold(NutritionalContent(
            calories = 0.0, carbohydrate = 0.0, protein = 0.0, fat = 0.0,
            saturatedFat = null, polyunsaturatedFat = null, monounsaturatedFat = null,
            transFat = null, cholesterol = null, sodium = null, potassium = null,
            fiber = null, sugar = null, addedSugars = null, vitaminD = null,
            vitaminA = null, vitaminC = null, calcium = null, iron = null
        )) { acc, ingredient ->
            val n = ingredient.serving?.normalizedNutritionalContent ?: return@fold acc
            val a = ingredient.amount
            acc.copy(
                calories            = acc.calories            + n.calories            * a,
                carbohydrate        = acc.carbohydrate        + n.carbohydrate        * a,
                protein             = acc.protein             + n.protein             * a,
                fat                 = acc.fat                 + n.fat                 * a,
                saturatedFat        = (acc.saturatedFat        ?: 0.0) + (n.saturatedFat        ?: 0.0) * a,
                polyunsaturatedFat  = (acc.polyunsaturatedFat  ?: 0.0) + (n.polyunsaturatedFat  ?: 0.0) * a,
                monounsaturatedFat  = (acc.monounsaturatedFat  ?: 0.0) + (n.monounsaturatedFat  ?: 0.0) * a,
                transFat            = (acc.transFat            ?: 0.0) + (n.transFat            ?: 0.0) * a,
                cholesterol         = (acc.cholesterol         ?: 0.0) + (n.cholesterol         ?: 0.0) * a,
                sodium              = (acc.sodium              ?: 0.0) + (n.sodium              ?: 0.0) * a,
                potassium           = (acc.potassium           ?: 0.0) + (n.potassium           ?: 0.0) * a,
                fiber               = (acc.fiber               ?: 0.0) + (n.fiber               ?: 0.0) * a,
                sugar               = (acc.sugar               ?: 0.0) + (n.sugar               ?: 0.0) * a,
                addedSugars         = (acc.addedSugars         ?: 0.0) + (n.addedSugars         ?: 0.0) * a,
                vitaminD            = (acc.vitaminD            ?: 0.0) + (n.vitaminD            ?: 0.0) * a,
                vitaminA            = (acc.vitaminA            ?: 0.0) + (n.vitaminA            ?: 0.0) * a,
                vitaminC            = (acc.vitaminC            ?: 0.0) + (n.vitaminC            ?: 0.0) * a,
                calcium             = (acc.calcium             ?: 0.0) + (n.calcium             ?: 0.0) * a,
                iron                = (acc.iron                ?: 0.0) + (n.iron                ?: 0.0) * a
            )
        }

        fun Double.fmt() = if (this % 1.0 == 0.0) this.toInt().toString() else "%.1f".format(this)
        fun Double?.line(label: String, unit: String) =
            this?.let { "$label: ${it.fmt()} $unit" }

        return listOfNotNull(
            "Calories: ${totals.calories.fmt()} kcal",
            "Carbohydrate: ${totals.carbohydrate.fmt()} g",
            "Protein: ${totals.protein.fmt()} g",
            "Fat: ${totals.fat.fmt()} g",
            totals.saturatedFat       .line("Saturated Fat",       "g"),
            totals.polyunsaturatedFat .line("Polyunsaturated Fat", "g"),
            totals.monounsaturatedFat .line("Monounsaturated Fat", "g"),
            totals.transFat           .line("Trans Fat",           "g"),
            totals.cholesterol        .line("Cholesterol",         "mg"),
            totals.sodium             .line("Sodium",              "mg"),
            totals.potassium          .line("Potassium",           "mg"),
            totals.fiber              .line("Fiber",               "g"),
            totals.sugar              .line("Sugar",               "g"),
            totals.addedSugars        .line("Added Sugars",        "g"),
            totals.vitaminA           .line("Vitamin A",           "mcg"),
            totals.vitaminC           .line("Vitamin C",           "mg"),
            totals.vitaminD           .line("Vitamin D",           "mcg"),
            totals.calcium            .line("Calcium",             "mg"),
            totals.iron               .line("Iron",                "mg")
        ).joinToString("\n")
    }

    fun refreshRecipes() {
        recipeRepo.refreshRecipes()
    }

    fun refreshUsers() {
        userRepo.refreshUsers()
    }
}
