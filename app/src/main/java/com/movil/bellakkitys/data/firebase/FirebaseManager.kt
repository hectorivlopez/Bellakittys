package com.movil.bellakkitys.data.firebase

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.movil.bellakkitys.data.auth.User
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.data.model.Song
import com.squareup.picasso.Picasso
import java.util.UUID

class FirebaseManager {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val users = db.collection("users")
    private val songs = db.collection("songs")
    private val artists = db.collection("artists")

    private val storage = Firebase.storage
    private val storageRef = storage.getReference()


    // ------------------------------ Auth ------------------------------
    fun signIn(email: String, password: String, callback: (Result<User?>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getCurrentUser { user ->
                        callback(Result.success(user))
                    }

                } else {
                    callback(Result.failure(task.exception ?: Exception("Login failed")))
                }
            }
    }

    fun signUp(name: String, email: String, password: String, callback: (Result<User?>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val account = auth.currentUser

                    // Create user
                    val uniqueID = UUID.randomUUID().toString()
                    val userData = hashMapOf(
                        "accountId" to account?.uid,
                        "name" to name,
                        "email" to email,
                        "rol" to "user",
                        "imageUrl" to ""
                    )
                    users.document(uniqueID).set(userData)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Create user", "DocumentSnapshot added")

                        }
                        .addOnFailureListener { e ->
                            Log.w("Create user", "Error adding document", e)
                        }

                    // Return data in object
                    val user = User(uniqueID, account?.uid, name, email, "user", "")
                    callback(Result.success(user))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Signup failed")))
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        if (currentUser != null) return true
        else return false
    }

    fun getCurrentUser(callback: (User?) -> Unit) {
        val account = auth.currentUser
        if (account == null) {
            callback(null)
            return
        }

        // Use TaskCompletionSource to wait for the query to complete
        val tcs = TaskCompletionSource<DocumentSnapshot?>()

        users.whereEqualTo("email", account.email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    tcs.setResult(documents.documents[0])
                } else {
                    tcs.setResult(null)
                }
            }
            .addOnFailureListener { exception ->
                tcs.setException(exception)
            }

        tcs.task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                val data = result?.data

                val user = User(
                    result?.id,
                    data?.get("accountId").toString(),
                    data?.get("name").toString(),
                    data?.get("email").toString(),
                    data?.get("rol").toString(),
                    data?.get("imageUrl").toString()
                )
                callback(user)
            } else {
                callback(null)
            }
        }
    }

    // ------------------------------ Queries ------------------------------
    // ---------- Create ----------
    fun createSong(song: Song) {
        val uniqueID = UUID.randomUUID().toString()
        val data = hashMapOf(
            "title" to song.title,
            "artists" to song.artists,
            "imageUrl" to song.imageUrl,
            "duration" to song.duration,
            "fileUrl" to song.fileUrl,
        )
        songs.document(uniqueID).set(data)
            .addOnSuccessListener { documentReference ->
                Log.d("Create Song", "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w("Create Song", "Error adding document", e)
            }
    }

    fun createArtist(artist: Artist) {
        uploadImage(artist.imageUrl) { uri ->
            if (uri != null) {
                val uniqueID = UUID.randomUUID().toString()
                val data = hashMapOf(
                    "name" to artist.name,
                    "imageUrl" to uri,
                    "description" to artist.description,
                )
                artists.document(uniqueID).set(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Create Artist", "DocumentSnapshot added")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Create Artist", "Error adding document", e)
                    }
            }
        }


    }

    // ---------- Read ----------
    // Find
    private fun find(collection: CollectionReference, field: String, value: String, callback: (List<DocumentSnapshot>?) -> Unit) {
        val tcs = TaskCompletionSource<List<DocumentSnapshot>?>()

        collection.whereEqualTo(field, value)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    tcs.setResult(documents.documents)
                } else {
                    tcs.setResult(null)
                }
            }
            .addOnFailureListener { exception ->
                tcs.setException(exception)
            }

        tcs.task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result

                callback(result)
            } else {
                callback(null)
            }
        }
    }

    fun findUsers(field: String, value: String, callback: (List<DocumentSnapshot>?) -> Unit) {
        find(users, field, value) { result -> callback(result) }
    }

    fun findSongs(field: String, value: String, callback: (List<DocumentSnapshot>?) -> Unit) {
        find(songs, field, value) { result -> callback(result) }
    }

    fun findArtists(field: String, value: String, callback: (List<DocumentSnapshot>?) -> Unit) {
        find(artists, field, value) { result -> callback(result) }
    }

    // Find by id
    private fun findById(collection: CollectionReference, id: String, callback: (DocumentSnapshot?) -> Unit) {
        // Use TaskCompletionSource to wait for the query to complete
        val tcs = TaskCompletionSource<DocumentSnapshot?>()

        collection.document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tcs.setResult(document)
                } else {
                    tcs.setResult(null)
                }
            }
            .addOnFailureListener { exception ->
                tcs.setException(exception)
            }

        tcs.task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result

                callback(result)
            } else {
                callback(null)
            }
        }
    }

    fun findUserById(id: String, callback: (DocumentSnapshot?) -> Unit) {
        findById(users, id) { result -> callback(result) }
    }

    fun findSongById(id: String, callback: (DocumentSnapshot?) -> Unit) {
        findById(songs, id) { result -> callback(result) }
    }

    fun findArtistById(id: String, callback: (DocumentSnapshot?) -> Unit) {
        findById(artists, id) { result -> callback(result) }
    }

    // Get all
    private fun all(collection: CollectionReference, callback: (List<DocumentSnapshot>?) -> Unit) {
        // Use TaskCompletionSource to wait for the query to complete
        val tcs = TaskCompletionSource<List<DocumentSnapshot>?>()

        collection.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    tcs.setResult(documents.documents)
                } else {
                    tcs.setResult(null)
                }
            }
            .addOnFailureListener { exception ->
                tcs.setException(exception)
            }

        tcs.task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result

                callback(result)
            } else {
                callback(null)
            }
        }
    }

    fun allUsers(callback: (List<DocumentSnapshot>?) -> Unit) {
        all(users) { result -> callback(result) }
    }

    fun allSongs(callback: (List<DocumentSnapshot>?) -> Unit) {
        all(songs) { result -> callback(result) }
    }

    fun allArtists(callback: (List<DocumentSnapshot>?) -> Unit) {
        all(artists) { result -> callback(result) }
    }

    // ---------- Update ----------
    fun updateUser(user: User) {
        val userData = hashMapOf(
            "accountId" to user.accountId,
            "name" to user.name,
            "email" to user.email,
            "rol" to user.rol,
            "imageUrl" to user.imageUrl
        )
        users.document(user.id!!).set(userData)
            .addOnSuccessListener { documentReference ->
                Log.d("Update user", "DocumentSnapshot updated")

            }
            .addOnFailureListener { e ->
                Log.w("Update user", "Error adding document", e)
            }
    }

    // ---------- Delete ----------

    // ------------------------------ Upload files ------------------------------

    fun uploadImage(imageUri: String, callback: (String?) -> Unit) {
        val fileUri = Uri.parse(imageUri)

        val uniqueID = UUID.randomUUID().toString()
        val fileRef = storageRef.child("images/$uniqueID.png")

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                callback("images/$uniqueID.png")
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }

    fun loadImage(imagePath: String, imageView: ImageView) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child(imagePath)

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            Picasso.get().load(imageUrl).into(imageView)
        }.addOnFailureListener {
            // Handle any errors
        }
    }

}
