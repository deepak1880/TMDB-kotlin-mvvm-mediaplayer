package com.example.tmdbapp.service


import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.model.people.PersonMovieCredits
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonService {
    @GET("person/{person_id}")
    suspend fun getPersonDetail(@Path("person_id") id: Int) : Response<Person>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(@Path("person_id") id:Int) : Response<PersonMovieCredits>
}