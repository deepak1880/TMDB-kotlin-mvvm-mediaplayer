package com.example.tmdbapp.service

import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.Result
import com.example.tmdbapp.model.people.PersonMovieCredits
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): Response<Result>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): Response<Result>

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<Result>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): Response<Result>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") id : Int) : Response<MovieDetails>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") id : Int) : Response<PersonMovieCredits>

}