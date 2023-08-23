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
import com.example.tmdbapp.adapter.PersonMovieCreditsAdapter
import com.example.tmdbapp.extensions.FragmentHelper
import com.example.tmdbapp.extensions.performFragmentTransaction
import com.example.tmdbapp.helper.CalculationHelper.findAge
import com.example.tmdbapp.helper.HorizontalItemMarginDecoration
import com.example.tmdbapp.helper.RetrofitHelper.Companion.IMAGE_BASE_URL
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.repository.MovieRepositoryImpl
import com.example.tmdbapp.repository.PersonRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PersonFragment : Fragment() {

    var personId: Int = -1
    private var person: Person? = null
    private val movieRepository = MovieRepositoryImpl()
    private val personRepository = PersonRepositoryImpl()

    // views
    lateinit var profilePic: ImageView
    lateinit var personName: TextView
    lateinit var personRole: TextView
    lateinit var personDob: TextView
    lateinit var personPlaceOfBirth: TextView
    lateinit var personBiography: TextView
    lateinit var movieCreditsRecyclerView: RecyclerView
    lateinit var movieCreditsAdapter: PersonMovieCreditsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val a : Cast? = it.getParcelable("something")
            personId = it.getInt("person_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_person, container, false)

        profilePic = view.findViewById(R.id.person_image_profilePic)
        personName = view.findViewById(R.id.person_tv_personName)
        personRole = view.findViewById(R.id.person_tv_personRole)
        personBiography = view.findViewById(R.id.person_tv_biography)
        personDob = view.findViewById(R.id.person_tv_dateOfBirth)
        personPlaceOfBirth = view.findViewById(R.id.person_tv_placeOfBirth)
        movieCreditsRecyclerView = view.findViewById(R.id.person_rv_knownFor)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            person = getPersonDetail(personId)
            updateUiWithData(person)
        }
    }

    private fun updateUiWithData(person: Person?) {
        person?.let {
            profilePic.load("${IMAGE_BASE_URL}${person.profilePath}")
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
            val itemMarginDecoration = HorizontalItemMarginDecoration(20)
            movieCreditsRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            movieCreditsAdapter = PersonMovieCreditsAdapter { it ->
                val targetFragment = MovieDetailsFragment()
                navigateToMovieDetails(it.id, targetFragment)
            }
            movieCreditsRecyclerView.adapter = movieCreditsAdapter
            movieCreditsRecyclerView.addItemDecoration(itemMarginDecoration)
            getPersonMovieCredits(it.id)
        }
    }


    private fun getPersonMovieCredits(personId: Int) {
        lifecycleScope.launch {
            val movieCredits = withContext(Dispatchers.IO) {
                personRepository.getPersonMovieCredits(personId)
            }
            movieCredits?.let {
                movieCreditsAdapter.submitList(movieCredits)
            }
        }
    }

    private suspend fun getPersonDetail(id: Int): Person? {
        return personRepository.getPersonDetail(id)
    }

    private fun navigateToMovieDetails(movieId: Int, targetFragment: Fragment) {

        lifecycleScope.launch {
            val movieDetail = movieRepository.getMovieDetail(movieId)
            val bundle = Bundle().apply {
                putParcelable("movieDetail", movieDetail)
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