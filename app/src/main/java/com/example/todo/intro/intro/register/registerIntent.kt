package com.example.todo.intro.intro.register

sealed class registerIntent {

        data class RegisterUserIntent(
            val username: String,
            val email: String,
            val password: String,
            val confirmPassword: String) : registerIntent()

}