package com.example.tmdbapp.repository

import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person

interface PersonRepository {

    suspend fun getPersonDetail(id: Int): Person?

    suspend fun getPersonMovieCredits(personId: Int): List<Cast>?
}