package com.example.todo.intro.intro.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.intro.intro.database.addUserToFireStore
import com.example.todo.intro.intro.database.userData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class registerViewModel: ViewModel() {

    var channel = Channel<registerIntent>(Channel.UNLIMITED)
    private val _viewState = MutableStateFlow<registerViewState>(registerViewState.idle)
    val viewState: StateFlow<registerViewState> get() = _viewState

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome


    init {
        processIntent()
    }


    // Process Intent
    private fun processIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect {
                when (it) {
                    is registerIntent.RegisterUserIntent -> {
                    showLoading()
                            registerUser (it.username, it.email, it.password)
                }
                    }
            }
        }
    }

    // Register User (reduce function)
    private fun registerUser(username: String,
                             email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    addUserToFireStoreDB(uid, username, email, password)
                } else {
                    hideLoadingWithError("User registration failed.")
                }
            }
            .addOnFailureListener { exception ->
                hideLoadingWithError(exception.localizedMessage)
            }
    }


    private fun addUserToFireStoreDB(
        uid: String,
        username: String,
        email: String,
        password: String,
    ) {
        addUserToFireStore(
            userData(id = uid, username = username, email = email, password = password),
            addOnSuccessListener = {
                hideLoadingAndNavigateToHome(userData(uid,username, email, password))
            },
            addOnFailureListener = {
                hideLoadingWithError("Failed to store user data in Firestore.")
            }
        )
    }
    private fun showLoading() {
        _viewState.value = registerViewState.isLoading
    }

    private fun hideLoadingAndNavigateToHome(userData: userData) {
        _viewState.value = registerViewState.RegistrationSuccessAction(userData)
        _navigateToHome.value = true
    }

    private fun hideLoadingWithError(errorMessage: String) {
        _viewState.value = registerViewState.ErrorAction(errorMessage)
    }
}