package com.example.tmdbapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tmdbapp.R
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction


class MainActivity : AppCompatActivity() {
     var fragment : Fragment? = null
    private val  FRAGMENT_TAG = "fragmentOne";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment = if (savedInstanceState == null) {
            HomeFragment()
        } else {
            supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        }

        supportFragmentManager.performFragmentTransaction(
            R.id.home_container, HomeFragment(), FragmentHelper.ADD, false
        )
    }
}