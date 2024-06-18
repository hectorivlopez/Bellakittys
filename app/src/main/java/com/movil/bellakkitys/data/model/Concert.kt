package com.movil.bellakkitys.data.model
// Nombre del archivo: Event.kt
import java.time.LocalDateTime

class Concert(
    val id: Int,
    val name: String,
    val dateTime: LocalDateTime,
    val location: String,
    val participatingArtists: List<Artist>
)
