package com.movil.bellakkitys.data.model

import com.google.firebase.firestore.DocumentSnapshot
import com.movil.bellakkitys.data.auth.User
import com.movil.bellakkitys.data.firebase.FirebaseManager

// Nombre del archivo: Song.kt
class Song(
    var id: String,
    var title: String,
    var artists: ArrayList<Artist>,
    var imageUrl: String,
    var duration: String,
    var fileUrl: String
) {
    var active: Boolean = false

    companion object {
        val firebaseManager = FirebaseManager()

        // ------------------------------ Map to Object ------------------------------
        private fun getSongs(result: List<DocumentSnapshot>): List<Song> {
            val songs = result.map { song ->
                getSong(song)
            }

            return songs
        }

        private fun getSong(result: DocumentSnapshot): Song {
            val data = result.data

            val artistsIds = data?.get("artists") as List<String>
            val artists = ArrayList<Artist>()

            for (artistId in artistsIds) {
                Artist.findById(artistId) { artist ->
                    if (artist != null) {
                        artists.add(artist)
                    }
                }
            }

            val song = Song(
                result.id,
                data?.get("title").toString(),
                artists,
                data?.get("imageUrl").toString(),
                data?.get("description").toString(),
                data?.get("concert").toString(),
            )

            return song
        }

        // ------------------------------ Queries ------------------------------
        // Read
        fun findById(id: String, callback: (Song?) -> Unit) {
            firebaseManager.findSongById(id) { result ->
                if (result != null) {
                    val song = getSong(result)
                    callback(song)
                }
            }
        }

        fun find(key: String, value: String, callback: (Song) -> Unit) {
            firebaseManager.findSongs(key, value) { result ->
                if (result != null) {
                    val songData = result[0]
                    val song = getSong(songData)
                    callback(song)
                }
            }
        }

        fun findAll(key: String, value: String, callback: (List<Song>) -> Unit) {
            firebaseManager.findSongs(key, value) { result ->
                if (result != null) {
                    val songs = getSongs(result)
                    callback(songs)
                }
            }
        }

        fun all(callback: (List<Song>) -> Unit) {
            firebaseManager.allSongs() { result ->
                if (result != null) {
                    val songs = getSongs(result)
                    callback(songs)
                }
            }
        }
    }

    // Create
    fun add() {
        firebaseManager.createSong(this)
    }

    // Update
    fun update() {
        Song.findById(this.id!!) { oldSong ->
            if(oldSong != null) {
                if(oldSong.imageUrl != this.imageUrl) {
                    // Delete old image
                    firebaseManager.deleteImage(oldSong.imageUrl)

                    // Upload new image
                    firebaseManager.uploadImage(this.imageUrl) { uri ->
                        this.imageUrl = uri!!

                        firebaseManager.updateSong(this)
                    }
                }
                else {
                    firebaseManager.updateSong(this)
                }
            }
        }
    }

    // Delete
    fun delete() {
        firebaseManager.deleteSong(this.id) {}
    }
}
