package com.example.tmdbapp.repository

import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast

interface MovieRepository {

    suspend fun getTopRatedMovies() : List<Movie>?

    suspend fun getNowPlayingMovies() : List<Movie>?

    suspend fun getPopularMovies() : List<Movie>?

    suspend fun getUpcomingMovies() : List<Movie>?

    suspend fun getMovieDetail(id: Int): MovieDetails?

    suspend fun getCast(id : Int) : List<Cast>?
}