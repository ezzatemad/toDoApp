package com.example.todo.intro.intro.register

import com.example.todo.intro.intro.database.userData

sealed class registerViewState {

    object isLoading: registerViewState()
    data class RegistrationSuccessAction(
        val user: userData
    ) : registerViewState()
    data class ErrorAction(
        val errorMessage: String
    ) : registerViewState()

    //    object LoadingAction : RegistrationViewAction()
        object idle: registerViewState()
}