package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person

interface PersonRepository {

    suspend fun getPersonDetail(id: Int): ResponseHelper<Person>

    suspend fun getPersonMovieCredits(personId: Int): ResponseHelper<List<Cast>>
}