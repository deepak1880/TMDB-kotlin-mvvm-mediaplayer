package com.example.tmdbapp.home

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.tmdbapp.R
import com.example.tmdbapp.adapter.PersonMovieCreditsAdapter
import com.example.tmdbapp.databinding.FragmentPersonBinding
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.gone
import com.example.tmdbapp.extensions.noInternetSnackbar
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.CalculationHelper.findAge
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.NetworkHelper.IMAGE_BASE_URL
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.receivers.ConnectivityReceiver
import com.example.tmdbapp.viewmodel.PersonViewModel
import com.example.tmdbapp.viewmodel.PersonViewModelFactory


class PersonFragment : Fragment() {

    private var personId: Int = -1

    // views
    lateinit var profilePic: ImageView
    lateinit var personName: TextView
    lateinit var personRole: TextView
    lateinit var personDob: TextView
    lateinit var personPlaceOfBirth: TextView
    lateinit var personBiography: TextView
    lateinit var movieCreditsRecyclerView: RecyclerView
    lateinit var movieCreditsAdapter: PersonMovieCreditsAdapter

    private lateinit var connectivityReceiver: ConnectivityReceiver

    private lateinit var binding: FragmentPersonBinding

    lateinit var personViewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            personId = it.getInt("person_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPersonBinding.inflate(layoutInflater)
        val view = binding.root
        profilePic = binding.personImageProfilePic
        personName = binding.personTvPersonName
        personRole = binding.personTvPersonRole
        personBiography = binding.personTvBiography
        personDob = binding.personTvDateOfBirth
        personPlaceOfBirth = binding.personTvPlaceOfBirth
        movieCreditsRecyclerView = binding.personRvKnownFor

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityReceiver = ConnectivityReceiver { isConnected ->
            if (!isConnected) {
                noInternetSnackbar(view, requireContext()) {
                    loadData()
                }
            }
        }


        personViewModel =
            ViewModelProvider(this, PersonViewModelFactory(personId))[PersonViewModel::class.java]

        personViewModel.personDetail.observe(viewLifecycleOwner) { personResponse ->
            when (personResponse) {
                is ResponseHelper.Success -> {
                    updateUiWithData(personResponse.data)
                    binding.shimmerViewContainer.visibility = View.GONE
                }

                is ResponseHelper.Error -> {

                }

                is ResponseHelper.Loading -> {
                    binding.shimmerViewContainer.visibility = View.VISIBLE
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

    private fun updateUiWithData(person: Person?) {
        person?.let {
            //profilePic.load("${IMAGE_BASE_URL}${person.profilePath}")
            val imageLoader = ImageLoader.Builder(requireContext())
                .build()
            val imageRequest = ImageRequest.Builder(requireContext())
                .data("${IMAGE_BASE_URL}${person.profilePath}")
                .placeholder(R.drawable.placeholder_person)
                .target(profilePic)
                .build()
            imageLoader.enqueue(imageRequest)

            personName.text = person.name
            personRole.text = person.knownForDepartment
            personBiography.text = person.biography
            personPlaceOfBirth.text = person.placeOfBirth
            val age = findAge(person.birthday)
            personDob.text =
                age?.let {
                    resources.getString(
                        R.string.person_dob,
                        person.birthday,
                        age.toString()
                    )
                }
                    ?: "Age : NA"

            // recycler view
            val itemMarginDecoration = ItemMarginDecorationHelper.HorizontalItemMarginDecoration(20)
            movieCreditsRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            movieCreditsAdapter = PersonMovieCreditsAdapter { cast ->
                val targetFragment = MovieDetailsFragment()
                navigateToMovieDetails(cast.id, targetFragment)
            }
            movieCreditsRecyclerView.adapter = movieCreditsAdapter
            movieCreditsRecyclerView.addItemDecoration(itemMarginDecoration)

            personViewModel.movieCredits.observe(viewLifecycleOwner) { castListResponse ->
                when (castListResponse) {
                    is ResponseHelper.Success -> {
                        Log.e("Cast list: ", castListResponse.data.toString())
                        movieCreditsAdapter.submitList(castListResponse.data ?: emptyList())
                        binding.shimmerRvContainer.gone()
                    }

                    is ResponseHelper.Error -> {
                    }

                    is ResponseHelper.Loading -> {
                        binding.shimmerRvContainer.startShimmer()
                    }
                }
            }
        }
    }
    
    private fun navigateToMovieDetails(movieId: Int, targetFragment: Fragment) {
        val bundle = Bundle().apply {
            putInt("movieId", movieId)
        }
        targetFragment.arguments = bundle

        parentFragmentManager.performFragmentTransaction(
            R.id.home_container,
            targetFragment,
            FragmentHelper.REPLACE,
            true
        )
    }    private fun loadData() {
        personViewModel.getPersonDetail(personId)
        personViewModel.getMovieCredits(personId)
    }



}