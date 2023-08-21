package com.example.tmdbapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.tmdbapp.R
import com.example.tmdbapp.helper.CalculationHelper
import com.example.tmdbapp.helper.RetrofitHelper
import com.example.tmdbapp.model.people.Person
import kotlinx.coroutines.launch


class PersonFragment : Fragment() {

    var personId: Int = -1
    private var person: Person? = null
    private val personService = RetrofitHelper.personService

    // views
    lateinit var profilePic: ImageView
    lateinit var personName: TextView
    lateinit var personRole: TextView
    lateinit var personDob: TextView
    lateinit var personPlaceOfBirth: TextView
    lateinit var personBiography: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
            profilePic.load("${RetrofitHelper.IMAGE_BASE_URL}${person.profilePath}")
            personName.text = person.name
            personRole.text = person.knownForDepartment
            personBiography.text = person.biography
            personPlaceOfBirth.text = person.placeOfBirth
            val age = CalculationHelper.findAge(person.birthday)
            personDob.text = "${person.birthday} (${age} years old)"
        }
    }

    private suspend fun getPersonDetail(id: Int): Person? {
        try {
            val response = personService.getPersonDetail(id)
            if (response.isSuccessful) {
                return response.body()
            } else {
                // Handle error
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}