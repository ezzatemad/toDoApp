package com.example.todo.intro.intro.database

data class userData(
    val id: String ?= null,
    val username: String?= null,
    val email: String?= null,
    val password: String?= null
){
    companion object{
        const val USER = "user"
    }
}
