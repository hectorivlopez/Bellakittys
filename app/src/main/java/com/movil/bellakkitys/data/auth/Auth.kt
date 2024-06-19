package com.movil.bellakkitys.data.auth

import com.movil.bellakkitys.data.firebase.FirebaseManager

class Auth {
    companion object {
        private val firebaseManager = FirebaseManager()
        fun signIn(email: String, password: String, callback: (Result<User?>) -> Unit) {
            firebaseManager.signIn(email, password) { result ->
                callback(result)
            }
        }

        fun signUp(name: String, email: String, password: String, callback: (Result<User?>) -> Unit) {
            firebaseManager.signUp(name, email, password) { result -> callback(result)}
        }

        fun logOut() {
            firebaseManager.logout()
        }

        fun currentUser(callback: (User?) -> Unit) {
            firebaseManager.getCurrentUser { result -> callback(result) }
        }
    }
}