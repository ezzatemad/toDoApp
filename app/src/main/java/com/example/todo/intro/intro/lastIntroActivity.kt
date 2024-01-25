package com.example.todo.intro.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.todo.R
import com.example.todo.intro.intro.login2.LoginActivity
import com.example.todo.intro.intro.register.registerActivity

class lastIntroActivity : AppCompatActivity() {
    lateinit var iv_back : ImageView
    lateinit var btn_register : Button
    lateinit var btn_login : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_intro)

        iv_back = findViewById(R.id.iv_back)
        btn_register = findViewById(R.id.btn_create_account)
        btn_login = findViewById(R.id.btn_login)
        iv_back.setOnClickListener {
            val intent = Intent(this@lastIntroActivity , Intro4Activity::class.java)
            startActivity(intent)
            finish()
        }
        btn_register.setOnClickListener {
            val intent = Intent(this@lastIntroActivity, registerActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login.setOnClickListener {
            val intent = Intent(this@lastIntroActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}