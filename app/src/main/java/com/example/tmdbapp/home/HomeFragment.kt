package com.example.tmdbapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.HomeAdapter
import com.example.tmdbapp.error.OfflineFragment
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.recyclerviews.HomeRecyclerView
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    // repository instance
    private val movieRepository = MovieRepositoryImpl()

    // progress bar
    lateinit var progressBar: ProgressBar

    // recycler view
    lateinit var homeRecyclerView: RecyclerView

    // home recyclerview adapter
    private val homeAdapter = HomeAdapter {
        navigateToMovieDetails(it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(context?.let { NetworkHelper.isInternetConnected(it) } == false){
            parentFragmentManager.performFragmentTransaction(
                R.id.home_container,
                OfflineFragment(),
                FragmentHelper.REPLACE,
                false
            )
        }

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val commonItemMarginDecoration = ItemMarginDecorationHelper.VerticalItemMarginDecoration(20)
        // Progress bar
        progressBar = view.findViewById(R.id.home_progressBar)
        // recycler view
        homeRecyclerView = view.findViewById(R.id.home_rv_homeRv)
        homeRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homeRecyclerView.setHasFixedSize(true)
        homeRecyclerView.addItemDecoration(commonItemMarginDecoration)
        homeRecyclerView.adapter = homeAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUIWithData()
    }

    private fun updateUIWithData() {
        lifecycleScope.launch {
            val nowPlayingMovies = getNowPlayingMovies()
            val topRatedMovies = getTopRatedMovies()
            val popularMovies = getPopularMovies()
            val upcomingMovies = getUpcomingMovies()

            // Update the adapter here with the fetched data
            val data = listOf(
                HomeRecyclerView("Now Playing", nowPlayingMovies),
                HomeRecyclerView("Top Rated", topRatedMovies),
                HomeRecyclerView("Popular", popularMovies),
                HomeRecyclerView("Upcoming", upcomingMovies)
            )
            homeAdapter.submitList(data)
            progressBar.visibility = View.GONE
        }
    }

    private suspend fun getTopRatedMovies(): List<Movie> {
        return withContext(Dispatchers.IO) {
            movieRepository.getTopRatedMovies()
        } ?: emptyList()
    }

    private suspend fun getNowPlayingMovies(): List<Movie> {
        return withContext(Dispatchers.IO) {
            movieRepository.getNowPlayingMovies()
        } ?: emptyList()
    }

    private suspend fun getPopularMovies(): List<Movie> {
        return withContext(Dispatchers.IO) {
            movieRepository.getPopularMovies()
        } ?: emptyList()
    }

    // properly written coroutine
    private suspend fun getUpcomingMovies(): List<Movie> {
        return withContext(Dispatchers.IO) {
            movieRepository.getUpcomingMovies()
        } ?: emptyList()
    }

    private suspend fun getMovieDetail(id: Int): MovieDetails? {
        return movieRepository.getMovieDetail(id)
    }


    private fun navigateToMovieDetails(id: Int) {
        val targetFragment = MovieDetailsFragment()
        lifecycleScope.launch {
            val movieDetail = getMovieDetail(id)
            movieDetail?.let {
                val bundle = Bundle().apply {
                    putParcelable("movieDetail", it)
                }
                targetFragment.arguments = bundle

                parentFragmentManager.performFragmentTransaction(
                    R.id.home_container,
                    targetFragment,
                    FragmentHelper.REPLACE,
                    true
                )
            }
        }
    }
}



