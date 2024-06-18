package com.movil.bellakkitys.utils

object Validator {
    fun isValidEmail(email: String?): Boolean {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String?): Boolean {
        return password != null && password.length >= 6
    }
}
