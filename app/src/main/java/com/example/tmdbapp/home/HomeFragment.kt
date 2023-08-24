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
import com.example.tmdbapp.adapter.MovieAdapter
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.HorizontalItemMarginDecoration
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    // repository instance
    private val movieRepository = MovieRepositoryImpl()

    // progress bar
    lateinit var progressBar: ProgressBar

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
        val commonItemMarginDecoration = HorizontalItemMarginDecoration(20)


        // Progress bar
        progressBar = view.findViewById(R.id.home_progressBar)
        // Now Playing section
        recyclerViewNowPlaying = view.findViewById(R.id.home_rv_nowPlaying)
        nowPlayingMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewNowPlaying.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNowPlaying.adapter = nowPlayingMovieAdapter
        recyclerViewNowPlaying.addItemDecoration(commonItemMarginDecoration)

        // Popular section
        recyclerViewPopular = view.findViewById(R.id.home_rv_popular)
        popularMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewPopular.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopular.adapter = popularMovieAdapter
        recyclerViewPopular.addItemDecoration(commonItemMarginDecoration)

        // Top Rated section
        recyclerViewTopRated = view.findViewById(R.id.home_rv_topRated)
        topRatedMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewTopRated.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTopRated.adapter = topRatedMovieAdapter
        recyclerViewTopRated.addItemDecoration(commonItemMarginDecoration)

        // Upcoming section
        recyclerViewUpcoming = view.findViewById(R.id.home_rv_upcoming)
        upcomingMovieAdapter = MovieAdapter {
            navigateToMovieDetails(it.id)
        }
        recyclerViewUpcoming.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpcoming.adapter = upcomingMovieAdapter
        recyclerViewUpcoming.addItemDecoration(commonItemMarginDecoration)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUIWithData()
    }

    private fun updateUIWithData() {
        getNowPlayingMovies()
        getTopRatedMovies()
        getPopularMovies()
        getUpcomingMovies()
    }

    private fun getTopRatedMovies() {
        lifecycleScope.launch {
            val movies = withContext(Dispatchers.IO) {
                movieRepository.getTopRatedMovies()
            }
            movies?.let { topRatedMovieAdapter.submitList(movies) }
        }
    }

    private fun getNowPlayingMovies() {
        lifecycleScope.launch {
            val movies = withContext(Dispatchers.IO) {
                movieRepository.getNowPlayingMovies()
            }
            movies?.let { nowPlayingMovieAdapter.submitList(movies) }
        }
    }

    private fun getPopularMovies() {
        lifecycleScope.launch {
            val movies = withContext(Dispatchers.IO) {
                movieRepository.getPopularMovies()
            }
            movies?.let { popularMovieAdapter.submitList(movies) }
        }
    }

    // properly written coroutine
    private fun getUpcomingMovies() {
        lifecycleScope.launch {
            val movies = withContext(Dispatchers.IO) {
                movieRepository.getUpcomingMovies()
            }
            movies?.let { upcomingMovieAdapter.submitList(movies) }
            progressBar.visibility = View.GONE
        }
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



