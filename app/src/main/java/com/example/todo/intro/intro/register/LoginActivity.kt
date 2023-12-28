package com.example.todo.intro.intro.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.todo.databinding.ActivityLoginBinding
import com.example.todo.intro.intro.indexScreens.HomeActivity
import com.example.todo.intro.intro.database.getUserFromFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var email: String
    lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            signInUserWithEmailAndPassword()
        }
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, registerActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun signInUserWithEmailAndPassword() {
        if (vaildatedUserLogin()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success")
                        val user = auth.currentUser
                        getUserFromFirestoreDB(user?.uid!!)
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

    fun getUserFromFirestoreDB(uid: String) {
            getUserFromFirestore(uid,
                addOnSuccessListener = {
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                addOnFailureListener = {
                    Toast.makeText(this, "Email or Password invaild", Toast.LENGTH_LONG).show()
                })
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