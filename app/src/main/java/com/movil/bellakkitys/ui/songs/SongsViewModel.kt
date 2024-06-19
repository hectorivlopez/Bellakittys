package com.movil.bellakkitys.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movil.bellakkitys.data.model.Song

class SongsViewModel : ViewModel() {
    var rol = "user"
    lateinit var songList: List<Song>

    var songs = ArrayList<Song>()


    private val _text = MutableLiveData<String>().apply {
        value = "This is songs Fragment"
    }
    val text: LiveData<String> = _text
}