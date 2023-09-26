package com.example.tmdbapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.videos.Video
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.Flow

class MovieDetailViewModel(movieId: Int) : ViewModel() {
    private val movieRepository = MovieRepositoryImpl()

    val movieDetail: Flow<ResponseHelper<MovieDetails>> = movieRepository.getMovieDetail(movieId)

    val castList: Flow<ResponseHelper<List<Cast>>> = movieRepository.getCast(movieId)

    val similarMovies: Flow<ResponseHelper<List<Movie>>> = movieRepository.getSimilarMovies(movieId)

    val movieVideos: Flow<ResponseHelper<List<Video>>> = movieRepository.getMovieVideos(movieId)
}