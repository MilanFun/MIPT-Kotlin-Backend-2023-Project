package service

import entity.User

class ServiceUser {
    var users: List<User> = ArrayList()

    fun save(user: User) : Boolean {
        users += user
        return true
    }

    fun getUserByUsername(name: String) : User? {
        for (i in users) {
            if (i.username == name) {
                return i
            }
        }
        return null
    }

    fun exist(user: User) : Boolean {
        return users.contains(user)
    }
}