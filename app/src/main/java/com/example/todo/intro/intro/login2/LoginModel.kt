package com.example.todo.intro.intro.login2

import com.example.todo.intro.intro.database.userData


data class LoginViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)


sealed class LoginViewIntent {
    data class LoginUserIntent(
        val email: String,
        val password: String) : LoginViewIntent()
}


sealed class LoginViewAction {
    object LoadingAction : LoginViewAction()
    data class loginSuccessAction(
        val user: userData
    ) : LoginViewAction()
    data class ErrorAction(
        val errorMessage: String
    ) : LoginViewAction()
}
