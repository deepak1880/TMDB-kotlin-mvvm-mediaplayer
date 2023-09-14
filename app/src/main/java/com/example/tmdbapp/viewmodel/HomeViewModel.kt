package com.example.tmdbapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val movieRepository = MovieRepositoryImpl()

    init {
        getPopularMovies()
        getUpcomingMovies()
        getTopRatedMovies()
        getNowPlayingMovies()
    }

    private val _popularMovies = MutableLiveData< ResponseHelper<List<Movie>>>()
    val popularMovies: LiveData<ResponseHelper<List<Movie>>> get() = _popularMovies

    private val _upcomingMovies = MutableLiveData< ResponseHelper<List<Movie>>>()
    val upcomingMovies: LiveData<ResponseHelper<List<Movie>>> get() = _upcomingMovies

    private val _topRatedMovies = MutableLiveData< ResponseHelper<List<Movie>>>()
    val topRatedMovies: LiveData<ResponseHelper<List<Movie>>> get() = _topRatedMovies

    private val _nowPlayingMovies = MutableLiveData< ResponseHelper<List<Movie>>>()
    val nowPlayingMovies: LiveData<ResponseHelper<List<Movie>>> get() = _nowPlayingMovies

    fun getPopularMovies() {
        viewModelScope.launch {
            val movieResponse = withContext(Dispatchers.IO) {
                movieRepository.getPopularMovies()
            }
            _popularMovies.postValue(movieResponse)
        }
    }

    fun getUpcomingMovies() {
        viewModelScope.launch {
            val movieResponse = withContext(Dispatchers.IO) {
                movieRepository.getUpcomingMovies()
            }
            _upcomingMovies.postValue(movieResponse)
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            _topRatedMovies.postValue(ResponseHelper.Loading())
            val movieResponse = withContext(Dispatchers.IO){
                movieRepository.getTopRatedMovies()
            }
            _topRatedMovies.postValue(movieResponse)
        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            val movieResponse = withContext(Dispatchers.IO) {
                movieRepository.getNowPlayingMovies()
            }
            _nowPlayingMovies.postValue(movieResponse)
        }
    }
}


