package com.example.todo.intro.intro.login2


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.intro.intro.database.getUserFromFirestore

import com.google.firebase.auth.FirebaseAuth


class LoginViewModel: ViewModel() {

    private val _viewState = MutableLiveData<LoginViewState>()
    val viewState: LiveData<LoginViewState> get() = _viewState


    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    fun processIntent(intent: LoginViewIntent) {
        when (intent) {
            is LoginViewIntent.LoginUserIntent -> loginUser(intent)
        }
    }
    private fun loginUser(intent: LoginViewIntent.LoginUserIntent) {
        _viewState.value = LoginViewState(isLoading = true)

        // Perform Firebase authentication
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(intent.email, intent.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    getUserFromFireStoreDB(user?.uid!!)
                    _viewState.value = LoginViewState(loginSuccess = true)
                } else {
                    _viewState.value = LoginViewState(errorMessage = task.exception?.message)
                }
            }
    }

    fun reduce(state: LoginViewState, action: LoginViewAction): LoginViewState {
        return when (action) {
            is LoginViewAction.LoadingAction -> state.copy(isLoading = true)
            is LoginViewAction.loginSuccessAction -> state.copy(isLoading = false, loginSuccess = true)
            is LoginViewAction.ErrorAction -> state.copy(isLoading = false, errorMessage = action.errorMessage)
        }
    }



    fun getUserFromFireStoreDB(uid: String) {
        getUserFromFirestore(uid,
            addOnSuccessListener = {
                _viewState.value = LoginViewState(loginSuccess = true)
                _navigateToHome.value = true
            },
            addOnFailureListener = {
                _viewState.value = LoginViewState(errorMessage = it.localizedMessage)
            })
    }


}