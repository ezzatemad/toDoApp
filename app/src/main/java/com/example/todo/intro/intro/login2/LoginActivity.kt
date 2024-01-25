package com.example.todo.intro.intro.login2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.databinding.ActivityLogin2Binding
import com.example.todo.intro.intro.indexScreens.HomeActivity
import com.example.todo.intro.intro.register.registerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding
    private lateinit var auth: FirebaseAuth
    lateinit var email: String
    lateinit var password: String
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        auth = Firebase.auth

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.viewState.observe(this, Observer { newState ->
            handleViewState(newState)
        })

        viewModel.navigateToHome.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        // Example in the activity
        binding.btnLogin.setOnClickListener {
            email = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()

            viewModel.processIntent(
                LoginViewIntent.LoginUserIntent(
                    email,
                    password
                )
            )
        }
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, registerActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun handleViewState(state: LoginViewState) {
        if (state.isLoading) {
            // Show loading progress
            showLoadingIndicator()
        } else if (state.loginSuccess) {
            // Registration successful, navigate to the next screen or perform other actions
            if (vaildatedUserLogin()) {
                viewModel.getUserFromFireStoreDB(auth.uid!!)
            }
        } else if (state.errorMessage != null) {
            // Show error message to the user
            hideLoadingIndicator()
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
    private fun showLoadingIndicator() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.visibility = View.GONE
    }
    fun vaildatedUserLogin():Boolean {

        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()
        if(email.isEmpty() || email.isBlank()){
            binding.etEmail.error = "Email Required"
            return false
        }
        else if(password.isEmpty() || password.isBlank()){
            binding.etPassword.error = "Password Required"
            return false
        }
        return true
    }
}