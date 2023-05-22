package com.apm.petmate.ui.favs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fragment de favoritos"
    }
    val text: LiveData<String> = _text
}