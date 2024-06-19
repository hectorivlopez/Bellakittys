package com.movil.bellakkitys.data.auth

import com.google.firebase.firestore.DocumentSnapshot
import com.movil.bellakkitys.data.firebase.FirebaseManager
import com.movil.bellakkitys.data.model.Artist

open class User(
    var id: String?,
    var accountId: String?,
    var name: String?,
    var email: String?,
    var rol: String?,
    var imageUrl: String?
) {
    companion object {
        val firebaseManager = FirebaseManager()

        // ------------------------------ Map to Object ------------------------------
        private fun getUsers(result: List<DocumentSnapshot>): List<User> {
            val users = result.map { user ->
                getUser(user)
            }

            return users
        }

        private fun getUser(result: DocumentSnapshot): User {
            val data = result.data

            val user = User(
                result.id,
                data?.get("accountId").toString(),
                data?.get("name").toString(),
                data?.get("email").toString(),
                data?.get("rol").toString(),
                data?.get("imageUrl").toString(),
            )

            return user
        }

        // ------------------------------ Queries ------------------------------
        // Read
        fun findById(id: String, callback: (User?) -> Unit) {
            firebaseManager.findUserById(id) { result ->
                if (result != null) {
                    val user = getUser(result)
                    callback(user)
                }
            }
        }

        fun find(key: String, value: String, callback: (User) -> Unit) {
            firebaseManager.findUsers(key, value) { result ->
                if (result != null) {
                    val userData = result[0]
                    val user = getUser(userData)
                    callback(user)
                }
            }
        }

        fun findAll(key: String, value: String, callback: (List<User>) -> Unit) {
            firebaseManager.findUsers(key, value) { result ->
                if (result != null) {
                    val users = getUsers(result)
                    callback(users)
                }
            }
        }

        fun all(callback: (List<User>) -> Unit) {
            firebaseManager.allUsers() { result ->
                if (result != null) {
                    val users = getUsers(result)
                    callback(users)
                }
            }
        }
    }

    // Update
    fun update() {
        User.findById(this.id!!) { oldUser ->
            if(oldUser != null) {
                if(oldUser.imageUrl != this.imageUrl) {
                    // Delete old photo

                    firebaseManager.uploadImage(this.imageUrl!!) { uri ->
                        this.imageUrl = uri

                        firebaseManager.updateUser(this)
                    }
                }
            }
        }


    }

}

