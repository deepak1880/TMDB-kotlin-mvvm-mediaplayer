package com.example.tmdbapp.home

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.HomeAdapter
import com.example.tmdbapp.databinding.FragmentHomeBinding
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.noInternetSnackbar
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.helper.Status
import com.example.tmdbapp.receivers.ConnectivityReceiver
import com.example.tmdbapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    // recycler view
    lateinit var homeRecyclerView: RecyclerView

    // home recyclerview adapter
    lateinit var homeAdapter: HomeAdapter

    // internet connectivity receiver
    private lateinit var connectivityReceiver: ConnectivityReceiver

    // view binding
    private lateinit var binding: FragmentHomeBinding

    //view model
    val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectivityReceiver = ConnectivityReceiver { isConnected ->
            if (!isConnected) {
                requireContext().noInternetSnackbar(view, requireContext()) {
                    loadData()
                }
            }
        }

        val commonItemMarginDecoration = ItemMarginDecorationHelper.VerticalItemMarginDecoration(20)
        // recycler view
        homeAdapter = HomeAdapter {
            navigateToMovieDetails(it.id)
        }
        homeRecyclerView = binding.homeRvHomeRv
        homeRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homeRecyclerView.setHasFixedSize(true)
        homeRecyclerView.addItemDecoration(commonItemMarginDecoration)
        homeRecyclerView.adapter = homeAdapter

        updateUIWithData()
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

    private fun updateUIWithData() {
        lifecycleScope.launch {
            homeViewModel.popularMovies.observe(viewLifecycleOwner) { popularMovieResponse ->

//                homeAdapter.submitData(HomeModel("Popular", popularMovieList))
                when (popularMovieResponse) {
                    is ResponseHelper.Success -> {
                        homeAdapter.updateDataItem(
                            0,
                            popularMovieResponse.data ?: emptyList(),
                            Status.SUCCESS
                        )
                    }

                    is ResponseHelper.Loading -> {
                        homeAdapter.updateDataItem(0, emptyList(), Status.LOADING)

                    }

                    is ResponseHelper.Error -> {
                        homeAdapter.updateDataItem(0, emptyList(), Status.ERROR)

                    }
                }

            }

            homeViewModel.topRatedMovies.observe(viewLifecycleOwner) { topRatedMovieResponse ->
//                homeAdapter.submitData(HomeModel("Top Rated", topRatedMovieList))
                when (topRatedMovieResponse) {
                    is ResponseHelper.Success -> {
                        homeAdapter.updateDataItem(
                            1, topRatedMovieResponse.data ?: emptyList(), Status.SUCCESS
                        )
                    }

                    is ResponseHelper.Loading -> {
                        homeAdapter.updateDataItem(1, emptyList(), Status.LOADING)
                    }

                    is ResponseHelper.Error -> {
                        homeAdapter.updateDataItem(1, emptyList(), Status.ERROR)
                    }
                }
            }

            homeViewModel.upcomingMovies.observe(viewLifecycleOwner) { upcomingMovieResponse ->
//                homeAdapter.submitData(HomeModel("Upcoming", upcomingMovieList))
                when (upcomingMovieResponse) {
                    is ResponseHelper.Success -> {
                        homeAdapter.updateDataItem(
                            2,
                            upcomingMovieResponse.data ?: emptyList(),
                            Status.SUCCESS
                        )
                    }

                    is ResponseHelper.Loading -> {
                        homeAdapter.updateDataItem(2, emptyList(), Status.LOADING)
                    }

                    is ResponseHelper.Error -> {
                        homeAdapter.updateDataItem(2, emptyList(), Status.ERROR)
                    }
                }

            }

            homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner) { nowPlayingMovieResponse ->
//                homeAdapter.submitData(HomeModel("Now Playing", nowPlayingMovieList))
                when (nowPlayingMovieResponse) {
                    is ResponseHelper.Success -> {
                        homeAdapter.updateDataItem(
                            3,
                            nowPlayingMovieResponse.data ?: emptyList(),
                            Status.SUCCESS
                        )
                    }

                    is ResponseHelper.Loading -> {
                        homeAdapter.updateDataItem(3, emptyList(), Status.LOADING)

                    }

                    is ResponseHelper.Error -> {
                        homeAdapter.updateDataItem(3, emptyList(), Status.ERROR)
                    }
                }
            }
        }
    }

    private fun navigateToMovieDetails(id: Int) {
        val targetFragment = MovieDetailsFragment()
        val bundle = Bundle().apply {
            putInt("movieId", id)
        }
        targetFragment.arguments = bundle
        parentFragmentManager.performFragmentTransaction(
            R.id.home_container,
            targetFragment,
            FragmentHelper.REPLACE,
            true
        )
    }

    private fun loadData() {
        homeViewModel.getUpcomingMovies()
        homeViewModel.getTopRatedMovies()
        homeViewModel.getNowPlayingMovies()
        homeViewModel.getPopularMovies()
    }

    private fun checkInternet() {
        connectivityReceiver = ConnectivityReceiver { isConnected ->
            if (!isConnected) {
                val snackbar = Snackbar.make(
                    binding.root, // Use requireView() to get the root view of the fragment's layout
                    "No internet connection",
                    Snackbar.LENGTH_INDEFINITE
                )

                snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onShown(transientBottomBar: Snackbar?) {
                        super.onShown(transientBottomBar)
                        transientBottomBar?.view?.findViewById<Button>(com.google.android.material.R.id.snackbar_action)
                            ?.setOnClickListener {
                                if (NetworkHelper.isInternetConnected(requireContext())) {
                                    Log.d("testing home frag", "inside snackbar action")
                                    loadData()
                                    snackbar.dismiss()
                                }
                            }

                    }
                })
                snackbar.setAction(
                    "Retry"
                ) {

                }
                snackbar.show()
            }
        }
    }
}


//            // Update the adapter here with the fetched data
//            val data = listOf(
//                HomeModel("Now Playing", homeViewModel.nowPlayingMovies.value),
//                HomeModel("Top Rated", homeViewModel.topRatedMovies.value),
//                HomeModel("Upcoming", homeViewModel.upcomingMovies.value),
//                HomeModel("Popular", homeViewModel.popularMovies.value)
//            )
//            homeAdapter.submitList(data)
//            homeViewModel.updateAllMovieCategories()


