package com.example.recipease.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FormValidator {

    private val fields = mutableListOf<FormField<*>>()
    private val _isValid = MutableLiveData(false)
    val isValid: LiveData<Boolean> = _isValid

    fun <T> addField(field: FormField<T>) {
        fields.add(field)
        field.bind { update() }
        update()
    }

    fun update() {
        _isValid.value = fields.all { it.evaluate() }
    }
}
