package com.example.todo.intro.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.todo.R

class Intro3Activity : AppCompatActivity() {
    lateinit var btn_back: Button
    lateinit var btn_next: Button
    lateinit var tv_skip: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro3)
        btn_back = findViewById(R.id.btn_intro_back)
        btn_next = findViewById(R.id.btn_intro_next)
        tv_skip = findViewById(R.id.tv_skip)
        btn_back.setOnClickListener {
            val intent = Intent(this@Intro3Activity,Intro2Activity::class.java)
            startActivity(intent)
            finish()
        }

        btn_next.setOnClickListener {
            val intent = Intent(this@Intro3Activity,Intro4Activity::class.java)
            startActivity(intent)
            finish()
        }

        tv_skip.setOnClickListener {
            val intent = Intent(this@Intro3Activity,lastIntroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}