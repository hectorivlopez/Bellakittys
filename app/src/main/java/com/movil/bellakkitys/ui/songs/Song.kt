package com.movil.bellakkitys.ui.songs

import com.movil.bellakkitys.ui.artists.Artist

// Nombre del archivo: Song.kt
class Song(
    var id: Int,
    var title: String,
    var artist: String,
    var image: Int,
    var duration: String,
    var file: Int
) {
    var active: Boolean = false
}
