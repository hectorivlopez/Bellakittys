package com.movil.bellakkitys.data.model

import com.google.firebase.firestore.DocumentSnapshot
import com.movil.bellakkitys.data.auth.User
import com.movil.bellakkitys.data.firebase.FirebaseManager
import com.movil.bellakkitys.data.model.Song

// Nombre del archivo: Artist.kt

class Artist(
    var id: String,
    var name: String,
    var imageUrl: String,
    var description: String,
) {
    companion object {
        val firebaseManager = FirebaseManager()

        // ------------------------------ Map to Object ------------------------------
        private fun getArtists(result: List<DocumentSnapshot>): List<Artist> {
            val artists = result.map { artist ->
                getArtist(artist)
            }

            return artists
        }

        private fun getArtist(result: DocumentSnapshot): Artist {
            val data = result.data

            val artist = Artist(
                result.id,
                data?.get("name").toString(),
                data?.get("imageUrl").toString(),
                data?.get("description").toString(),
            )

            return artist
        }

        // ------------------------------ Queries ------------------------------
        // Read
        fun findById(id: String, callback: (Artist?) -> Unit) {
            firebaseManager.findArtistById(id) { result ->
                if (result != null) {
                    val artist = getArtist(result)
                    callback(artist)
                }
            }
        }

        fun find(key: String, value: String, callback: (Artist) -> Unit) {
            firebaseManager.findArtists(key, value) { result ->
                if (result != null) {
                    val artistData = result[0]
                    val artist = getArtist(artistData)
                    callback(artist)
                }
            }
        }

        fun findAll(key: String, value: String, callback: (List<Artist>) -> Unit) {
            firebaseManager.findArtists(key, value) { result ->
                if (result != null) {
                    val artists = getArtists(result)
                    callback(artists)
                }
            }
        }

        fun all(callback: (List<Artist>) -> Unit) {
            firebaseManager.allArtists() { result ->
                if (result != null) {
                    val artists = getArtists(result)
                    callback(artists)
                }
            }
        }
    }

    // Create
    fun add() {
        firebaseManager.createArtist(this)
    }

    // Update
    fun update() {
        Artist.findById(this.id!!) { oldArtist ->
            if(oldArtist != null) {
                if(oldArtist.imageUrl != this.imageUrl) {
                    // Delete old image
                    firebaseManager.deleteImage(oldArtist.imageUrl)

                    // Upload new image
                    firebaseManager.uploadImage(this.imageUrl) { uri ->
                        this.imageUrl = uri!!

                        firebaseManager.updateArtist(this)
                    }
                }
                else {
                    firebaseManager.updateArtist(this)
                }
            }
        }
    }

    // Delete
    fun delete() {
        Song.firebaseManager.deleteArtist(this.id) {}
    }

}

