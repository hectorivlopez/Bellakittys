package com.movil.bellakkitys

import com.movil.bellakkitys.ui.artists.Artist
import com.movil.bellakkitys.ui.songs.Song
import java.time.LocalDateTime

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
