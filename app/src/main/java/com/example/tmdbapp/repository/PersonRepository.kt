package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    fun getPersonDetail(personId: Int): Flow<ResponseHelper<Person>>

    fun getPersonMovieCredits(personId: Int): Flow<ResponseHelper<List<Cast>>>
}