package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.videos.Video
import kotlinx.coroutines.flow.Flow
interface MovieRepository {

    fun getTopRatedMovies() : Flow<ResponseHelper<List<Movie>>>

    fun getNowPlayingMovies() : Flow<ResponseHelper<List<Movie>>>

    fun getPopularMovies() : Flow<ResponseHelper<List<Movie>>>

    fun getUpcomingMovies() : Flow<ResponseHelper<List<Movie>>>

    fun getMovieDetail(id: Int): Flow<ResponseHelper<MovieDetails>>

    fun getCast(id : Int) : Flow<ResponseHelper<List<Cast>>>

    fun getSimilarMovies(id : Int) : Flow<ResponseHelper<List<Movie>>>

    fun getMovieVideos(id : Int) : Flow<ResponseHelper<List<Video>>>
}