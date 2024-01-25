package com.example.todo.intro.intro.register

import com.example.todo.intro.intro.database.userData

data class RegistrationViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registrationSuccess: Boolean = false
)


sealed class RegistrationViewIntent {
    data class RegisterUserIntent(
        val username: String,
        val email: String,
        val password: String) : RegistrationViewIntent()
}


sealed class RegistrationViewAction {
    object LoadingAction : RegistrationViewAction()
    data class RegistrationSuccessAction(
        val user: userData
    ) : RegistrationViewAction()
    data class ErrorAction(
        val errorMessage: String
    ) : RegistrationViewAction()
}
