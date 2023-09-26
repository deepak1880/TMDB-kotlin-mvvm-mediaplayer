package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.service.PersonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PersonRepositoryImpl : PersonRepository {

    val personService : PersonService= NetworkHelper.personService

    override fun getPersonDetail(personId: Int): Flow<ResponseHelper<Person>> =flow{
        val result = personService.getPersonDetail(personId)
        if (result.isSuccessful) {
            emit(ResponseHelper.Success(result.body()))
        } else {
            emit(ResponseHelper.Error("Failed to fetch person details"))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(ResponseHelper.Error(it.message.toString()))
    }

    override fun getPersonMovieCredits(personId: Int): Flow<ResponseHelper<List<Cast>>> = flow{
        val result = personService.getPersonMovieCredits(personId)
        if (result.isSuccessful) {
            emit(ResponseHelper.Success(result.body()?.cast?: emptyList()))
        } else {
            emit(ResponseHelper.Error("Failed to fetch person's movie credit."))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(ResponseHelper.Error(it.message.toString()))
    }
}
