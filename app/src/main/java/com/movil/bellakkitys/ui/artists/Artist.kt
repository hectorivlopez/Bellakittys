package com.movil.bellakkitys.ui.artists

import com.movil.bellakkitys.ui.concerts.Concert
import com.movil.bellakkitys.ui.songs.Song

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

