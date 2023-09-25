package com.example.tmdbapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(private val movieRepository : MovieRepository) : ViewModel(){

    val popularMovies:Flow<ResponseHelper<List<Movie>>> = movieRepository.getTopRatedMovies()

    val topRatedMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getTopRatedMovies()

    val upcomingMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getUpcomingMovies()

    val nowPlayingMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getNowPlayingMovies()

    init {
        popularMovies
        topRatedMovies
        upcomingMovies
        nowPlayingMovies
    }

}


