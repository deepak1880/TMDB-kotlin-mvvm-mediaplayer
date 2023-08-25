package com.example.tmdbapp.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tmdbapp.R
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.NetworkHelper.isInternetConnected
import com.example.tmdbapp.home.HomeFragment

class OfflineFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_offline, container, false)
        val retryButton: Button = view.findViewById(R.id.offline_btn_retry)
        lateinit var targetFragment: Fragment
        retryButton.setOnClickListener {
            if (isInternetConnected(view.context)) {
                targetFragment = HomeFragment()
                parentFragmentManager.performFragmentTransaction(
                    R.id.home_container,
                    targetFragment,
                    FragmentHelper.REPLACE,
                    true
                )
            } else {
                Toast.makeText(context, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}