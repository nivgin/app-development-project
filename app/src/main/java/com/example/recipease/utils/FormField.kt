package com.example.recipease.utils

import android.view.View

class FormField<T>(
    val view: View,
    val valueProvider: () -> T,
    val validator: (T) -> Boolean,
    val attachListener: (onChange: () -> Unit) -> Unit
) {
    var isValid: Boolean = false
        private set

    fun evaluate(): Boolean {
        isValid = validator(valueProvider())
        return isValid
    }

    fun bind(onChange: () -> Unit) {
        attachListener {
            evaluate()
            onChange()
        }
    }
}
