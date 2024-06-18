package com.movil.bellakkitys.data.firebase

import android.util.Log
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.movil.bellakkitys.data.auth.User
import java.util.UUID

class FirebaseManager {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val users = db.collection("users")

    // ------------------------------ Auth ------------------------------
    fun signIn(email: String, password: String, callback: (Result<User?>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getCurrentUser{user ->
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
                    val uniqueID =  UUID.randomUUID().toString()
                    val userData = hashMapOf(
                        "accountId" to account?.uid,
                        "name" to name,
                        "email" to email,
                        "rol" to "user",
                    )
                    users.document(uniqueID).set(userData)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Create user", "DocumentSnapshot added")

                        }
                        .addOnFailureListener { e ->
                            Log.w("Create user", "Error adding document", e)
                        }

                    // Return data in object
                    val user = User(uniqueID, account?.uid, name, email, "user")
                    callback(Result.success(user))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Signup failed")))
                }
            }
    }

    fun logout () {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        if(currentUser != null) return true
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
                    data?.get("rol").toString()
                )
                callback(user)
            } else {
                callback(null)
            }
        }
    }

    private fun query(collection: CollectionReference, field: String, value: String, callback: (List<DocumentSnapshot>?) -> Unit) {
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

    fun getUsers(field: String, value: String, callback: (List<DocumentSnapshot>?) -> Unit) {
        query(users, field, value) {result -> callback(result)}
    }



}
