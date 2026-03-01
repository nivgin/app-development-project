package com.example.recipease.utils

import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView

// EditText field
fun EditText.asFormField(validator: (String) -> Boolean): FormField<String> {
    return FormField(
        view = this,
        valueProvider = { text.toString() },
        validator = validator,
        attachListener = { onChange ->
            this.doOnTextChanged { _, _, _, _ -> onChange() }
        }
    )
}

// RecyclerView list field
fun <T> RecyclerView.asListField(
    provider: () -> List<T>,
    validator: (List<T>) -> Boolean
): FormField<List<T>> {
    return FormField(
        view = this,
        valueProvider = provider,
        validator = validator,
        attachListener = { /* manual trigger only */ }
    )
}

// ImageView field
fun ImageView.asImageField(validator: (Boolean) -> Boolean): FormField<Boolean> {
    return FormField(
        view = this,
        valueProvider = { this.drawable != null },
        validator = validator,
        attachListener = { /* manual trigger only */ }
    )
}
