package com.example.tmdbapp.repository

import android.accounts.NetworkErrorException
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.service.PersonService

class PersonRepositoryImpl : PersonRepository {

    val personService : PersonService= NetworkHelper.personService

    override suspend fun getPersonDetail(id: Int): Person? {
        try {
            val response = personService.getPersonDetail(id)
            if (response.isSuccessful) {
                return response.body()
            } else {
                throw NetworkErrorException("Failed to fetch person's details")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override suspend fun getPersonMovieCredits(personId: Int): List<Cast>? {
        try {
            val response = personService.getPersonMovieCredits(personId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                return responseBody?.cast ?: emptyList()
            } else {
                throw NetworkErrorException("Failed to fetch person's movie credits")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
