package com.example.tmdbapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.repository.PersonRepositoryImpl
import kotlinx.coroutines.flow.Flow

class PersonViewModel(personId: Int) : ViewModel() {

    private val personRepository = PersonRepositoryImpl()

    val personDetail: Flow<ResponseHelper<Person>> =
        personRepository.getPersonDetail(personId)

    val movieCredits: Flow<ResponseHelper<List<Cast>>> =
        personRepository.getPersonMovieCredits(personId)


}