package com.example.todo.intro.intro.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.todo.databinding.ActivityRegisterBinding
import com.example.todo.intro.intro.indexScreens.HomeActivity
import com.example.todo.intro.intro.database.addUserToFireStore
import com.example.todo.intro.intro.database.userData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class registerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    lateinit var email: String
    lateinit var username: String
    lateinit var password: String
    lateinit var confirmPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.btnRegister.setOnClickListener {
            createUserWithEmailAndPassword()
        }
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@registerActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    fun addUserToFireStoreDB(uid: String){
        addUserToFireStore(
            userData(id = uid,username = username, email = email, password= password),
            addOnSuccessListener = {
                val intent = Intent(this@registerActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            },
            addOnFailureListener = {
                Log.w("TAG", it.localizedMessage ?:  "")
            }
            )
    }

    fun vaildatedUser():Boolean {
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

    private fun createUserWithEmailAndPassword() {
        if (vaildatedUser()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success")
                        val user = auth.currentUser
                        addUserToFireStoreDB(user?.uid!!)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
}