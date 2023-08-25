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
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.CastAdapter
import com.example.tmdbapp.adapter.MovieDetailsViewPagerAdapter
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.repository.MovieRepositoryImpl
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsFragment : Fragment() {

    private val movieRepository = MovieRepositoryImpl()
    var movieDetails: MovieDetails? = null

    private lateinit var recyclerViewCast: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private lateinit var movieCover: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieTag: TextView
    private lateinit var movieOverviewBody: TextView
    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager2
    lateinit var viewPagerAdapter: MovieDetailsViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieDetails = arguments?.getParcelable("movieDetail")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        movieCover = view.findViewById(R.id.detail_image_cover)
        movieTitle = view.findViewById(R.id.detail_tv_title)
        movieTag = view.findViewById(R.id.detail_tv_tagline)
        movieOverviewBody = view.findViewById(R.id.detail_tv_overviewBody)
        recyclerViewCast = view.findViewById(R.id.detail_rv_castBody)

        viewPager = view.findViewById(R.id.detail_viewPager)
        tabLayout = view.findViewById(R.id.detail_tabLayout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateUI()
    }

    private fun updateUI() {
        val itemMarginDecoration = ItemMarginDecorationHelper.HorizontalItemMarginDecoration(20)

        // backdrop
        movieCover.load(NetworkHelper.IMAGE_BASE_URL + movieDetails?.backdrop_path)
        // movie title + release year (extracted year from release data)
        val releaseDate = movieDetails?.release_date.toString()
        val titleText = movieDetails?.title.toString()
        val yearText = "(${releaseDate.substring(0, 4)})"
        movieTitle.text = "${titleText} ${yearText}"
        // movie tag
        movieTag.text = getString(R.string.detail_tagline, movieDetails?.tagline)
        // overview body
        movieOverviewBody.text = movieDetails?.overview
        // recycler view for cast

        recyclerViewCast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        castAdapter = CastAdapter {
            navigateToCastFragment(it.id)
        }
        recyclerViewCast.adapter = castAdapter
        recyclerViewCast.addItemDecoration(itemMarginDecoration)

        //update the cast recycler view and update tab layout
        movieDetails?.let {
            getCast(it.id)
            updateViewPagerData(it.id)
        }

    }

    private fun updateViewPagerData(id : Int) {
        viewPagerAdapter = MovieDetailsViewPagerAdapter(childFragmentManager, lifecycle,id)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "More like this"
                1 -> tab.text = "Trailer & More"
            }
        }.attach()
    }

    private fun navigateToCastFragment(id: Int) {
        val targetFragment = PersonFragment()
        val bundle = Bundle().apply {
            putInt("person_id", id)
        }
        targetFragment.arguments = bundle

        parentFragmentManager.performFragmentTransaction(
            R.id.home_container, targetFragment, FragmentHelper.REPLACE, true
        )
    }


    private fun getCast(id: Int) {
        lifecycleScope.launch {
            val castList = withContext(Dispatchers.IO) {
                movieRepository.getCast(id)
            }
            castList?.let {
                castAdapter.submitList(it)
            }
        }
    }

}