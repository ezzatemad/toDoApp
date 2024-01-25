package com.example.todo.intro.intro.indexScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.intro.intro.fragment.bottomSheet.bottomSheetFragment
import com.example.todo.intro.intro.fragment.calenderFragment
import com.example.todo.intro.intro.fragment.foucesFragment
import com.example.todo.intro.intro.fragment.indexFragment.indexFragment
import com.example.todo.intro.intro.profile.profileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var btn_floating : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
    }

    private fun initView()
    {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        pushFragment(indexFragment())
        bottomNavigationView.setOnItemSelectedListener {
            if(it.itemId == R.id.menu_home)
            {
                pushFragment(indexFragment())
            }
            else if(it.itemId == R.id.menu_calender)
            {
                pushFragment(calenderFragment())
            }
            else if(it.itemId == R.id.menu_focus)
            {
                pushFragment(foucesFragment())
            }
            else if(it.itemId == R.id.menu_profile)
            {
                pushFragment(profileFragment())
            }
            return@setOnItemSelectedListener true
        }
        btn_floating = findViewById(R.id.btn_floating)
        btn_floating.setOnClickListener {
            val bottomSheet = bottomSheetFragment()
            bottomSheet.show(supportFragmentManager, "add_to_do")
            bottomNavigationView.isEnabled = false
        }

    }


    private fun pushFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().replace(R.id.fl_container,fragment).commit()
    }
}