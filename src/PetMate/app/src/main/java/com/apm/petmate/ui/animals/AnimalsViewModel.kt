package com.apm.petmate.ui.animals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnimalsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fragment de listado de animales"
    }
    val text: LiveData<String> = _text
}