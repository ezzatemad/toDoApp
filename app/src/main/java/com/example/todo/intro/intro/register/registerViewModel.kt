package com.example.todo.intro.intro.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.intro.intro.database.addUserToFireStore
import com.example.todo.intro.intro.database.userData
import com.google.firebase.auth.FirebaseAuth
class registerViewModel: ViewModel() {
    private val _viewState = MutableLiveData<RegistrationViewState>()
    val viewState: LiveData<RegistrationViewState> get() = _viewState

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome


    fun processIntent(intent: RegistrationViewIntent) {
        when (intent) {
            is RegistrationViewIntent.RegisterUserIntent -> registerUser(intent)
        }
    }
    private fun registerUser(intent: RegistrationViewIntent.RegisterUserIntent) {
        _viewState.value = RegistrationViewState(isLoading = true)

        // Perform Firebase registration
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(intent.email, intent.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    addUserToFirestoreDB(user?.uid, intent.username, intent.email, intent.password)
                    _viewState.value = RegistrationViewState(registrationSuccess = true)

                } else {
                    _viewState.value = RegistrationViewState(errorMessage = task.exception?.message)
                }
            }
    }
    fun reduce(state: RegistrationViewState, action: RegistrationViewAction): RegistrationViewState {
        return when (action) {
            is RegistrationViewAction.LoadingAction -> state.copy(isLoading = true)
            is RegistrationViewAction.RegistrationSuccessAction -> state.copy(isLoading = false, registrationSuccess = true)
            is RegistrationViewAction.ErrorAction -> state.copy(isLoading = false, errorMessage = action.errorMessage)
        }
    }
     fun addUserToFirestoreDB(uid: String?,
                              username: String,
                              email: String,
                              password: String) {
        addUserToFireStore(
            userData(id = uid ?: "", username = username, email = email, password = password),
            addOnSuccessListener = {
                _viewState.value = RegistrationViewState(registrationSuccess = true)
                _navigateToHome.value = true  // Set the navigation event to true

            },
            addOnFailureListener = {
                _viewState.value = RegistrationViewState(errorMessage = it.localizedMessage)
            }
        )
    }

}