package com.movil.bellakkitys

import com.movil.bellakkitys.ui.artists.Artist
import com.movil.bellakkitys.ui.songs.Song

open class User(
     var id: Int,
     var username: String,
     var email: String,
     var password: String,
     var favoriteSongs: List<Song>,
     var favoriteArtists: List<Artist>,
     var rol: String
)