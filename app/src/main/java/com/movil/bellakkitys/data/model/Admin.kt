package com.movil.bellakkitys.data.model

import com.movil.bellakkitys.data.model.User
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.data.model.Song

class Admin(
    id: Int,
    username: String,
    email: String,
    password: String,
    favoriteSongs: List<Song>,
    favoriteArtists: List<Artist>,
    rol: String,
    var adminTitle: String,
    var adminPermissions: List<String>,
    var adminLastLogin: Long,
    var adminTasksCompleted: Int,
    var adminDepartment: String
) : User(id, username, email, password, emptyList(), emptyList(), "Admin")
