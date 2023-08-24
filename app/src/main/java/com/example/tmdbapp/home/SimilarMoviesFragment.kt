package com.example.tmdbapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.MovieAdapter
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.GridItemMarginDecoration
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimilarMoviesFragment : Fragment() {
    var movieId: Int? = null
    private val movieRepository = MovieRepositoryImpl()
    lateinit var recyclerView: RecyclerView
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movie id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_similar_movies, container, false)
        val commonItemMarginDecoration = GridItemMarginDecoration(10)
        recyclerView = view.findViewById(R.id.similarMovies_rv_movies)
        movieAdapter = MovieAdapter {
            val targetFragment = MovieDetailsFragment()
            navigateToMovieDetails(it.id, targetFragment)
        }
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = movieAdapter
        recyclerView.addItemDecoration(commonItemMarginDecoration)
        return view
    }

    private fun navigateToMovieDetails(movieId: Int, targetFragment: MovieDetailsFragment) {
        lifecycleScope.launch {
            val movieDetail = movieRepository.getMovieDetail(movieId)
            val bundle = Bundle().apply {
                putParcelable("movieDetail", movieDetail)
            }
            targetFragment.arguments = bundle

            requireActivity().supportFragmentManager.performFragmentTransaction(
                R.id.home_container,
                targetFragment,
                FragmentHelper.REPLACE,
                true
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId?.let {
            updateUiWithData(it)
        }
    }

    private fun updateUiWithData(movieId: Int) {
        lifecycleScope.launch {
            val movies = withContext(Dispatchers.IO) {
                movieRepository.getSimilarMovies(movieId)
            }
            movies?.let {
                movieAdapter.submitList(it)
            }
        }
    }
}
