package com.example.tmdbapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.TrailerAndMoreAdapter
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrailersAndMoreFragment : Fragment() {

    val movieRepository = MovieRepositoryImpl()
    var movieId : Int? = null

    lateinit var recyclerView: RecyclerView
    val trailerAndMoreAdapter = TrailerAndMoreAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId=arguments?.getInt("movie id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trailers, container, false)
        val verticalItemMarginDecoration =
            ItemMarginDecorationHelper.VerticalItemMarginDecoration(10)
        recyclerView = view.findViewById(R.id.trailer_rv_videos)
        recyclerView.adapter = trailerAndMoreAdapter
        recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        recyclerView.addItemDecoration(verticalItemMarginDecoration)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId?.let { getMovieVideos(it) }
    }


    private fun getMovieVideos(id : Int) {
        lifecycleScope.launch {
            val movieCredits = withContext(Dispatchers.IO) {
                movieRepository.getMovieVideos(id)
            }
            movieCredits?.let {
                trailerAndMoreAdapter.submitList(movieCredits)
            }
        }
    }

}