package com.example.tmdbapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.MovieAdapter
import com.example.tmdbapp.databinding.FragmentSimilarMoviesBinding
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.receivers.ConnectivityReceiver
import com.example.tmdbapp.viewmodel.MovieDetailViewModel
import com.example.tmdbapp.viewmodel.MovieDetailViewModelFactory
import kotlinx.coroutines.launch

class SimilarMoviesFragment : Fragment() {
    var movieId: Int = -1
    lateinit var recyclerView: RecyclerView

    lateinit var movieAdapter: MovieAdapter

    private lateinit var connectivityReceiver: ConnectivityReceiver

    private lateinit var binding: FragmentSimilarMoviesBinding

    lateinit var movieDetailsViewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movie_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSimilarMoviesBinding.inflate(layoutInflater)
        val view = binding.root


        val commonItemMarginDecoration = ItemMarginDecorationHelper.GridItemMarginDecoration(10)
        recyclerView = binding.similarMoviesRvMovies
        movieAdapter = MovieAdapter {
            val targetFragment = MovieDetailsFragment()
            navigateToMovieDetails(it.id, targetFragment)
        }
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = movieAdapter
        recyclerView.addItemDecoration(commonItemMarginDecoration)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailsViewModel =
            ViewModelProvider(
                this,
                MovieDetailViewModelFactory(movieId)
            )[MovieDetailViewModel::class.java]

        updateUiWithData()
    }


    private fun updateUiWithData() {
        lifecycleScope.launch {
            movieDetailsViewModel.similarMovies.observe(viewLifecycleOwner) { similarMovieResponse ->
                when (similarMovieResponse) {
                    is ResponseHelper.Success -> {
                        Log.d("similar movies","${similarMovieResponse.data?.get(8)}")
                        movieAdapter.submitList(similarMovieResponse.data)
                    }

                    is ResponseHelper.Error -> {

                    }

                    is ResponseHelper.Loading -> {

                    }
                }

            }
        }
    }

    private fun navigateToMovieDetails(movieId: Int, targetFragment: MovieDetailsFragment) {
        val bundle = Bundle().apply {
            putInt("movieId", movieId)
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
