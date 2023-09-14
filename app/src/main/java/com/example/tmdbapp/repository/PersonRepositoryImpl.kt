package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.service.PersonService

class PersonRepositoryImpl : PersonRepository {

    val personService : PersonService= NetworkHelper.personService

    override suspend fun getPersonDetail(personId: Int): ResponseHelper<Person> {
        return try {
            val response = personService.getPersonDetail(personId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody)
            } else {
                ResponseHelper.Error("Failed to fetch person's details")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getPersonMovieCredits(personId: Int): ResponseHelper<List<Cast>> {
        return try {
            val response = personService.getPersonMovieCredits(personId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.cast)
            } else {
                ResponseHelper.Error("Failed to fetch person's movie credits")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }
}
