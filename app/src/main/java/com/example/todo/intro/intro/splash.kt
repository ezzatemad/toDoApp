package com.example.todo.intro.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.todo.R
import com.example.todo.intro.intro.database.getUserFromFirestore
import com.example.todo.intro.intro.database.userData
import com.example.todo.intro.intro.indexScreens.HomeActivity
import com.example.todo.intro.intro.login2.LoginActivity
import com.example.todo.intro.intro.register.dataUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class splash : AppCompatActivity() {
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        whichNavigate()

    }

    fun isUserLogged():Boolean{
        return auth.currentUser != null
    }

    fun whichNavigate(){
        if(isUserLogged()){
            //get user data and navigate to home screen
            getUserFromFirestore(auth.currentUser?.uid!!,
                addOnSuccessListener = {
                    dataUtils.userData = it.toObject(userData::class.java)
                    dataUtils.firebaseUser = auth.currentUser
                    val intent =Intent(this@splash , HomeActivity::class.java)
                    startActivity(intent)
                    finish()
            },
                addOnFailureListener = {
                    val intent = Intent(this@splash, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
            })
        }
        else{
            Handler(mainLooper).postDelayed({
                val intent = Intent(this@splash, Intro2Activity::class.java)
                startActivity(intent)
                finish()
            },2000)

        }
    }
}
