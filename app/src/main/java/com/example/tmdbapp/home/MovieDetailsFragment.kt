package com.example.tmdbapp.home

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.CastAdapter
import com.example.tmdbapp.adapter.MovieDetailsViewPagerAdapter
import com.example.tmdbapp.databinding.FragmentMovieDetailsBinding
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.gone
import com.example.tmdbapp.extensions.noInternetSnackbar
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.extensions.visible
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.receivers.ConnectivityReceiver
import com.example.tmdbapp.viewmodel.MovieDetailViewModel
import com.example.tmdbapp.viewmodel.MovieDetailViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {
    var movieId: Int = -1

    private lateinit var recyclerViewCast: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private lateinit var movieCover: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieTag: TextView
    private lateinit var movieOverviewBody: TextView
    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager2
    lateinit var viewPagerAdapter: MovieDetailsViewPagerAdapter

    private lateinit var connectivityReceiver: ConnectivityReceiver

    private lateinit var binding: FragmentMovieDetailsBinding

    lateinit var movieDetailsViewModel: MovieDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movieId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        movieDetailsViewModel =
            ViewModelProvider(
                this,
                MovieDetailViewModelFactory(movieId)
            )[MovieDetailViewModel::class.java]

        binding = FragmentMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        movieCover = binding.detailImageCover
        movieTitle = binding.detailTvTitle
        movieTag = binding.detailTvTagline
        movieOverviewBody = binding.detailTvOverviewBody
        recyclerViewCast = binding.detailRvCastBody

        viewPager = binding.detailViewPager
        tabLayout = binding.detailTabLayout

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        connectivityReceiver = ConnectivityReceiver { isConnected ->
            if (!isConnected) {
                requireContext().noInternetSnackbar(view, requireContext()) {
                    loadData()
                }
            }
        }

        lifecycleScope.launch {
            movieDetailsViewModel.movieDetail.observe(viewLifecycleOwner) { movieDetails ->
                when (movieDetails) {
                    is ResponseHelper.Success -> {
                        updateUI(movieDetails.data!!)
                        binding.topMovieDetailsGroup.visible()
                        binding.detailImageCover.visible()
                        binding.shimmerMovieDetailContainer.stopShimmer()
                        binding.shimmerMovieDetailContainer.gone()
                    }

                    is ResponseHelper.Error -> {
                    }

                    is ResponseHelper.Loading -> {
                        binding.movieDetailCard.gone()
                        binding.detailImageCover.gone()

                        binding.shimmerMovieDetailContainer.gone()
                        //binding.shimmerMovieDetailContainer.startShimmer()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        requireActivity().registerReceiver(connectivityReceiver, filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
         requireActivity().unregisterReceiver(connectivityReceiver)
    }

    private fun updateUI(movieDetails: MovieDetails) {
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
        val itemMarginDecoration = ItemMarginDecorationHelper.HorizontalItemMarginDecoration(20)
        recyclerViewCast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        castAdapter = CastAdapter {
            navigateToCastFragment(it.id)
        }
        recyclerViewCast.adapter = castAdapter
        recyclerViewCast.addItemDecoration(itemMarginDecoration)

        //update the cast recycler view and update tab layout
        updateCastList()
        updateViewPagerData(movieDetails.id)

    }

    private fun updateViewPagerData(id: Int) {
        viewPagerAdapter = MovieDetailsViewPagerAdapter(childFragmentManager, lifecycle, id)
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

    private fun updateCastList() {
        lifecycleScope.launch {
            movieDetailsViewModel.castList.observe(viewLifecycleOwner) { castListResponse ->
                when (castListResponse) {
                    is ResponseHelper.Success -> {
                        castAdapter.submitList(castListResponse.data ?: emptyList())
                        binding.shimmerRvCastContainer.gone()
                    }

                    is ResponseHelper.Error -> {

                    }

                    is ResponseHelper.Loading -> {
                        binding.shimmerRvCastContainer.visible()
                    }
                }
            }
        }
    }

   fun loadData(){
       movieDetailsViewModel.getMovieDetail(movieId)
       movieDetailsViewModel.getSimilarMovies(movieId)
       movieDetailsViewModel.getCast(movieId)
   }

}