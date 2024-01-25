package com.example.todo.intro.intro.Repository.User

import com.example.todo.intro.intro.database.userData

interface UserRepository {


    suspend fun getUserById(userId: String): userData?
    suspend fun addUser(user: userData)
}