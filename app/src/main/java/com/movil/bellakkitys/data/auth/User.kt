package com.movil.bellakkitys.data.auth

import com.movil.bellakkitys.data.firebase.FirebaseManager

open class User (var id: String?, var accountId: String?, var name: String?, var email: String?, var rol: String?) {
     companion object {
          val firebaseManager = FirebaseManager()

          fun query(key: String, value: String, callback: (List<User>) -> Unit) {
               firebaseManager.getUsers(key, value) {result ->
                    if(result != null) {
                         val users = result.map { user ->
                              val data = user.data
                              User(
                                   user.id,
                                   data?.get("accountId").toString(),
                                   data?.get("name").toString(),
                                   data?.get("email").toString(),
                                   data?.get("rol").toString()
                              )
                         }
                         callback(users)
                    }
               }
          }
     }
}

