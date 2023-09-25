package com.example.tmdbapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.adapter.TrailerAndMoreAdapter
import com.example.tmdbapp.databinding.FragmentTrailersBinding
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.receivers.ConnectivityReceiver
import com.example.tmdbapp.viewmodel.MovieDetailViewModel
import com.example.tmdbapp.viewmodel.MovieDetailViewModelFactory
import kotlinx.coroutines.launch

class TrailersAndMoreFragment : Fragment() {

    private var movieId: Int = -1

    lateinit var recyclerView: RecyclerView

    private val trailerAndMoreAdapter = TrailerAndMoreAdapter()

    private lateinit var connectivityReceiver: ConnectivityReceiver

    private lateinit var binding: FragmentTrailersBinding

    lateinit var movieDetailsViewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movie_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrailersBinding.inflate(layoutInflater)
        val view = binding.root


        //Recycler view
        val verticalItemMarginDecoration =
            ItemMarginDecorationHelper.VerticalItemMarginDecoration(10)
        recyclerView = binding.trailerRvVideos
        recyclerView.adapter = trailerAndMoreAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(verticalItemMarginDecoration)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailsViewModel = ViewModelProvider(
            this,
            MovieDetailViewModelFactory(movieId)
        )[MovieDetailViewModel::class.java]

        // update the UI
        updateUiWithData()
    }

    override fun onResume() {
        super.onResume()
       /* val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        requireActivity().registerReceiver(connectivityReceiver, filter)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        requireActivity().unregisterReceiver(connectivityReceiver)
    }

    private fun updateUiWithData() {
        lifecycleScope.launch {
            movieDetailsViewModel.movieVideos.collect { movieVideoResponse ->
                when (movieVideoResponse) {
                    is ResponseHelper.Success -> {
                        Log.d("video data from api",movieVideoResponse.data.toString())
                        trailerAndMoreAdapter.submitList(movieVideoResponse.data ?: emptyList())
                    }

                    is ResponseHelper.Error -> {

                    }

                    is ResponseHelper.Loading -> {

                    }
                }
            }
        }
    }
}
