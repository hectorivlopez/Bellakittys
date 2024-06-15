package com.movil.bellakkitys.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SongsViewModel : ViewModel() {
    var rol = "user"
    lateinit var songList: ArrayList<Song>

    var songs = ArrayList<Song>()


    private val _text = MutableLiveData<String>().apply {
        value = "This is songs Fragment"
    }
    val text: LiveData<String> = _text
}