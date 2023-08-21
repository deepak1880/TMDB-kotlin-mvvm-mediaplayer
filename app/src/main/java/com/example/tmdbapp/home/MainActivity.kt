package com.example.tmdbapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tmdbapp.R
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction


class MainActivity : AppCompatActivity() {
     var fragment : Fragment? = null
    val  FRAGMENT_TAG = "fragmentOne";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            fragment = HomeFragment()
        } else {
            fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        }

        supportFragmentManager.performFragmentTransaction(
            R.id.home_container, HomeFragment(), FragmentHelper.ADD, true
        )
    }
}