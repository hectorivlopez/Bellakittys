package com.movil.bellakkitys.ui.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movil.bellakkitys.data.model.Artist

class ArtistsViewModel : ViewModel() {
    var rol = "user"
    lateinit var artistList: ArrayList<Artist>

    var artist = ArrayList<Artist>()

    var artistImage: Int? = null
    var artistName = ""
    var artistDescription = ""

    private val _text = MutableLiveData<String>().apply {
        value = "This is artists Fragment"
    }
    val text: LiveData<String> = _text
}