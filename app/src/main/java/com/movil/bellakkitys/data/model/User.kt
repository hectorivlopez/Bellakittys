package com.movil.bellakkitys.data.model

open class User(
     var id: Int,
     var username: String,
     var email: String,
     var password: String,
     var favoriteSongs: List<Song>,
     var favoriteArtists: List<Artist>,
     var rol: String
)