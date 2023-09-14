package com.example.tmdbapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdbapp.databinding.ActivityMainBinding
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

         if (savedInstanceState == null) {
             supportFragmentManager.performFragmentTransaction(
                 binding.homeContainer.id, HomeFragment(), FragmentHelper.ADD, false
             )
        }
    }
}