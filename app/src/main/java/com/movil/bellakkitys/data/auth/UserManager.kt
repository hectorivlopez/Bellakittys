package com.movil.bellakkitys.data.auth

import android.content.Context

object UserManager {
    private var user: User? = null

    fun init(context: Context) {
        val preferences = UserPreferences(context)
        user = preferences.getUser()
    }

    fun setUser(user: User, context: Context) {
        UserManager.user = user
        UserPreferences(context).saveUser(user)
    }

    fun getUser(): User? {
        return user
    }

    fun clearUser(context: Context) {
        user = null
        UserPreferences(context).clear()
    }
}
