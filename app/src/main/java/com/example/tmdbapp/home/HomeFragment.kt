package com.example.tmdbapp.home

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.MovieAdapter
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.RetrofitHelper
import com.example.tmdbapp.model.MovieDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private val movieService = RetrofitHelper.movieService

    // All recycler views
    lateinit var recyclerViewTopRated: RecyclerView
    lateinit var recyclerViewNowPlaying: RecyclerView
    lateinit var recyclerViewPopular: RecyclerView
    lateinit var recyclerViewUpcoming: RecyclerView

    // All the adapters
    private lateinit var nowPlayingMovieAdapter: MovieAdapter
    private lateinit var topRatedMovieAdapter: MovieAdapter
    private lateinit var popularMovieAdapter: MovieAdapter
    private lateinit var upcomingMovieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Now Playing section
        recyclerViewNowPlaying = view.findViewById(R.id.home_rv_nowPlaying)
        nowPlayingMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewNowPlaying.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNowPlaying.adapter = nowPlayingMovieAdapter

        // Popular section
        recyclerViewPopular = view.findViewById(R.id.home_rv_popular)
        popularMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewPopular.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopular.adapter = popularMovieAdapter

        // Top Rated section
        recyclerViewTopRated = view.findViewById(R.id.home_rv_topRated)
        topRatedMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewTopRated.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTopRated.adapter = topRatedMovieAdapter

        // Upcoming section
        recyclerViewUpcoming = view.findViewById(R.id.home_rv_upcoming)
        upcomingMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewUpcoming.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpcoming.adapter = upcomingMovieAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNowPlayingMovies()
        getTopRatedMovies()
        getPopularMovies()
        getUpcomingMovies()
    }


    private fun getTopRatedMovies() {
        lifecycleScope.launch {
            try {
                val response = movieService.getTopRatedMovies()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val movies = responseBody?.movies ?: emptyList()
                    topRatedMovieAdapter.submitList(movies)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getNowPlayingMovies() {
        lifecycleScope.launch {
            try {
                val response = movieService.getNowPlayingMovies()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val movies = responseBody?.movies ?: emptyList()
                    nowPlayingMovieAdapter.submitList(movies)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPopularMovies() {
        lifecycleScope.launch {
            try {
                val response = movieService.getPopularMovies()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val movies = responseBody?.movies ?: emptyList()
                    popularMovieAdapter.submitList(movies)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // properly written coroutine
    private fun getUpcomingMovies() {
        lifecycleScope.launch {
            try {
                val movies =withContext(Dispatchers.IO){
                    val response = movieService.getUpcomingMovies()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.movies ?: emptyList()

                } else {
                    throw NetworkErrorException("Failed to fetch upcoming movies")
                }
            }
            upcomingMovieAdapter.submitList(movies)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getMovieDetail(id: Int): MovieDetails? {
            try {
                val response = movieService.getMovieDetail(id)
                if (response.isSuccessful) {
                    return response.body()
                } else {
                    Log.d("Movie-Detail", "Failed : ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return null
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



