package com.movil.bellakkitys.data.auth

import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.data.model.Song

class Admin(
    id: String,
    accountId: String,
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
) : User(id, accountId, username, email, "admin", "")
