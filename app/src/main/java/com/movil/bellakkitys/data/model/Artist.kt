package com.movil.bellakkitys.data.model

import com.movil.bellakkitys.data.model.Song

// Nombre del archivo: Artist.kt

class Artist(
    var id: Int,
    var name: String,
    var image: Int,
    var imageString: String,
    var description: String,
    var discography: ArrayList<Song>,
    var concert: String
)

