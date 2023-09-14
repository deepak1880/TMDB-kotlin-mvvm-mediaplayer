package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.videos.Video

interface MovieRepository {

    suspend fun getTopRatedMovies() : ResponseHelper<List<Movie>>

    suspend fun getNowPlayingMovies() : ResponseHelper<List<Movie>>

    suspend fun getPopularMovies() : ResponseHelper<List<Movie>>

    suspend fun getUpcomingMovies() : ResponseHelper<List<Movie>>

    suspend fun getMovieDetail(id: Int): ResponseHelper<MovieDetails>

    suspend fun getCast(id : Int) : ResponseHelper<List<Cast>>

    suspend fun getSimilarMovies(id : Int) : ResponseHelper<List<Movie>>

    suspend fun getMovieVideos(id : Int) : ResponseHelper<List<Video>>
}