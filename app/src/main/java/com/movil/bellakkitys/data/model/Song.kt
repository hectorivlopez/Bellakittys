package com.movil.bellakkitys.data.model

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
