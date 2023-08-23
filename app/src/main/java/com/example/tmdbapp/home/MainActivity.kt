package com.example.tmdbapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdbapp.R
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         if (savedInstanceState == null) {
             supportFragmentManager.performFragmentTransaction(
                 R.id.home_container, HomeFragment(), FragmentHelper.ADD, false
             )
        }
    }
}