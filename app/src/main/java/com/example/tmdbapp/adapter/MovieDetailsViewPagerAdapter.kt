package com.example.tmdbapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tmdbapp.home.SimilarMoviesFragment
import com.example.tmdbapp.home.TrailersAndMoreFragment

class MovieDetailsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val movieIdParam: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SimilarMoviesFragment()

            1 -> TrailersAndMoreFragment()

            else -> SimilarMoviesFragment()
        }.apply {
            arguments = Bundle().apply { putInt("movie id", movieIdParam) }
        }
    }
}