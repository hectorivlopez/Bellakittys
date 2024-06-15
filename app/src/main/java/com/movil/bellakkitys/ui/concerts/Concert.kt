package com.movil.bellakkitys.ui.concerts
// Nombre del archivo: Event.kt
import com.movil.bellakkitys.ui.artists.Artist
import java.time.LocalDateTime

class Concert(
    val id: Int,
    val name: String,
    val dateTime: LocalDateTime,
    val location: String,
    val participatingArtists: List<Artist>
)
