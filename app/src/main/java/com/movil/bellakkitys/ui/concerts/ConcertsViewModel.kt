package com.movil.bellakkitys.ui.concerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConcertsViewModel : ViewModel() {
    var rol = "user"

    private val _text = MutableLiveData<String>().apply {
        value = "This is concerts Fragment"
    }
    val text: LiveData<String> = _text
}