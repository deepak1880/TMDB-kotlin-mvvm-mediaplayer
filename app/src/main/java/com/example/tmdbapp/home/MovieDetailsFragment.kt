package com.example.tmdbapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.CastAdapter
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.RetrofitHelper
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {

    private val movieService = RetrofitHelper.movieService
    var movieDetails: MovieDetails? = null
    var cast : List<Cast> = emptyList()

    private lateinit var recyclerViewCast: RecyclerView
    private lateinit var castAdapter: CastAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieDetails = arguments?.getParcelable("movieDetail")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val movieCover = view.findViewById<ImageView>(R.id.detail_image_cover)
        val movieTitle = view.findViewById<TextView>(R.id.detail_tv_title)
        val movieTag = view.findViewById<TextView>(R.id.detail_tv_tagline)
        val movieOverviewBody = view.findViewById<TextView>(R.id.detail_tv_overviewBody)

        // backdrop
        movieCover.load(RetrofitHelper.IMAGE_BASE_URL + movieDetails?.backdrop_path)
        // movie title + release year (extracted year from release data)
        val releaseDate = movieDetails?.release_date.toString()
        val titleText = movieDetails?.original_title.toString()
        val yearText = "(${releaseDate.substring(0, 4)})"
        movieTitle.text = "${titleText} ${yearText}"
        // movie tag
        movieTag.text = getString(R.string.detail_tagline, movieDetails?.tagline)
        // overview body
        movieOverviewBody.text = movieDetails?.overview
        // recycler view for cast
        recyclerViewCast = view.findViewById(R.id.detail_rv_castBody)
        recyclerViewCast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        castAdapter = CastAdapter {
            navigateToCastFragment(it.id)
        }
        recyclerViewCast.adapter = castAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        movieDetails?.id?.let { getCast(it) }
    }


    private fun navigateToCastFragment(id: Int) {
        val targetFragment = PersonFragment()
        val bundle = Bundle().apply {
            putInt("person_id",id)
        }
        targetFragment.arguments = bundle

        parentFragmentManager.performFragmentTransaction(
            R.id.home_container,
            targetFragment,
            FragmentHelper.REPLACE,
            true
        )
    }

    private fun getCast(id: Int) {
        lifecycleScope.launch {
            try {
                val response = movieService.getMovieCredits(id)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    cast = responseBody?.cast ?: emptyList()
                    castAdapter.submitList(cast)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}