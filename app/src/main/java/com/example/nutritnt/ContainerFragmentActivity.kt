package com.example.nutritnt


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.add

class ContainerFragmentActivity :  AppCompatActivity(R.layout.activity_container_fragment) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    //add<WelcomeFragment>(R.id.fragmentContainer)
                    add<LoginFragment>(R.id.fragmentContainer)
                }
            }
        }
}