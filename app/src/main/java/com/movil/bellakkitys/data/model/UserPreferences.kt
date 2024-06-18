package com.movil.bellakkitys.data.model

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        preferences.edit().apply {
            putString("id", user.id)
            putString("name", user.name)
            putString("email", user.email)
            apply()
        }
    }

    fun getUser(): User? {
        val id = preferences.getString("id", null)
        val name = preferences.getString("name", null)
        val email = preferences.getString("email", null)

        return if (id != null && email != null && name != null) {
            User(id, name, email)
        } else {
            null
        }
    }

    fun clear() {
        preferences.edit().clear().apply()
    }
}
