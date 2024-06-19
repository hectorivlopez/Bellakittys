package com.movil.bellakkitys.data.auth

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        preferences.edit().apply {
            putString("id", user.id)
            putString("accountId", user.accountId)
            putString("name", user.name)
            putString("email", user.email)
            putString("rol", user.rol)
            putString("imageUrl", user.imageUrl)
            apply()
        }
    }

    fun getUser(): User? {
        val id = preferences.getString("id", null)
        val accountId = preferences.getString("accountId", null)
        val name = preferences.getString("name", null)
        val email = preferences.getString("email", null)
        val rol = preferences.getString("rol", null)
        val imageUrl = preferences.getString("imageUrl", null)

        return if (id != null && accountId != null && email != null && name != null && rol != null) {
            User(id, accountId, name, email, rol, imageUrl)
        } else {
            null
        }
    }

    fun clear() {
        preferences.edit().clear().apply()
    }
}
