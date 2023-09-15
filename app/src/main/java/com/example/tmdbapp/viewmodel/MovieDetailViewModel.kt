package com.example.tmdbapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.videos.Video
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailViewModel(movieId: Int) : ViewModel() {
    private val movieRepository = MovieRepositoryImpl()

    private val _movieDetail = MutableLiveData<ResponseHelper<MovieDetails>>()
    val movieDetail: LiveData<ResponseHelper<MovieDetails>> get() = _movieDetail

    private val _castList = MutableLiveData<ResponseHelper<List<Cast>>>()
    val castList: LiveData<ResponseHelper<List<Cast>>> get() = _castList

    private val _similarMovies = MutableLiveData<ResponseHelper<List<Movie>>>()
    val similarMovies: LiveData<ResponseHelper<List<Movie>>> get() = _similarMovies

    private val _movieVideos = MutableLiveData<ResponseHelper<List<Video>>>()
    val movieVideos: LiveData<ResponseHelper<List<Video>>> get() = _movieVideos

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _movieDetail.postValue(ResponseHelper.Loading())
            val movieDetailResponse = withContext(Dispatchers.IO) {
                movieRepository.getMovieDetail(movieId)
            }
            _movieDetail.postValue(movieDetailResponse)
        }
    }

    fun getCast(movieId: Int) {
        viewModelScope.launch {
            _movieDetail.postValue(ResponseHelper.Loading())
            val castListResponse = withContext(Dispatchers.IO) {
                movieRepository.getCast(movieId)
            }
            _castList.postValue(castListResponse)
        }
    }

    fun getSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            _movieDetail.postValue(ResponseHelper.Loading())
            val similarMoviesResponse = withContext(Dispatchers.IO) {
                movieRepository.getSimilarMovies(movieId)
            }
            _similarMovies.postValue(similarMoviesResponse)
        }
    }

    fun getMovieVideos(movieId: Int) {
        viewModelScope.launch {
            val movieVideoResponse = withContext(Dispatchers.IO) {
                movieRepository.getMovieVideos(movieId)
            }
            _movieVideos.postValue(movieVideoResponse)
        }
    }

    init {
        getMovieDetail(movieId)
        getSimilarMovies(movieId)
        getMovieVideos(movieId)
        getCast(movieId)
    }
}