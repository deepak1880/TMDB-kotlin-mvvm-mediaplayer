package com.example.tmdbapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.repository.MovieRepository
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {
    private val movieRepository: MovieRepository = MovieRepositoryImpl()
    val popularMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getPopularMovies()

    val topRatedMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getTopRatedMovies()

    val upcomingMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getUpcomingMovies()

    val nowPlayingMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getNowPlayingMovies()
}


