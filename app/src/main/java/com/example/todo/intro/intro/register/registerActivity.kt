package com.example.todo.intro.intro.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.todo.databinding.ActivityRegisterBinding
import com.example.todo.intro.intro.indexScreens.HomeActivity
import com.example.todo.intro.intro.login2.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class registerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    lateinit var email: String
    lateinit var username: String
    lateinit var password: String
    lateinit var confirmPassword: String
    private lateinit var viewModel: registerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[registerViewModel::class.java]
        // Initialize Firebase Auth
        auth = Firebase.auth
        render()

//        viewModel.viewState.observe(this, Observer { newState ->
//            handleViewState(newState)
//        })

        viewModel.navigateToHome.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                hideLoadingIndicator()
                val intent = Intent(this@registerActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        })


        // Example in the activity
        binding.btnRegister.setOnClickListener {
            showLoadingIndicator()
            if (validatedUser()) {
                val username = binding.etUsername.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val confirmPassword = binding.etConfirmPassword.text.toString()

                lifecycleScope.launch {
                    viewModel.channel.send(
                        registerIntent.RegisterUserIntent(username, email, password,confirmPassword)
                    )
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@registerActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun render(){
        lifecycleScope.launch {
            viewModel.viewState.collect {
                when (it) {
                    is registerViewState.isLoading -> showLoadingIndicator()
                    is registerViewState.RegistrationSuccessAction -> {
                        hideLoadingIndicator()
                        viewModel.navigateToHome.observe(
                            this@registerActivity,
                            Observer { shouldNavigate ->
                                if (shouldNavigate) {
                                    val intent =
                                        Intent(this@registerActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            })
                    }

                    is registerViewState.ErrorAction ->{
                        hideLoadingIndicator()
                }
                    is registerViewState.idle -> hideLoadingIndicator()
            }

            }
        }
    }

        private fun showLoadingIndicator() {
            binding.progressBar.visibility = View.VISIBLE
        }

        private fun hideLoadingIndicator() {
            binding.progressBar.visibility = View.GONE
        }
    fun validatedUser():Boolean {
        username = binding.etUsername.text.toString()
        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()
        confirmPassword = binding.etConfirmPassword.text.toString()
        if(email.isEmpty() || email.isBlank()){
            binding.etEmail.error = "Email Required"
            return false
        }
        else if(username.isEmpty() || username.isBlank()){
            binding.etUsername.error = "Username Required"
            return false
        }
        else if(password.isEmpty() || password.isBlank()){
            binding.etPassword.error = "Password Required"
            return false
        }
        else if(confirmPassword.isEmpty() || confirmPassword.isBlank())
        {
            binding.etConfirmPassword.error = "Confirm Password required"
            return false
        }
        else if(confirmPassword != password)
        {
            binding.etConfirmPassword.error = "Password must be identical"
            return false
        }
        return true
    }
}