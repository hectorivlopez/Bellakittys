package com.movil.bellakkitys

object UserManager {
    private var nextUserId = 0
    public val userList = mutableListOf<User>()

    fun addUser(user: User) {
        user.id = nextUserId++
        userList.add(user)
    }

    fun findUserByUsername(username: String): User? {
        return userList.find { it.username == username }
    }
}