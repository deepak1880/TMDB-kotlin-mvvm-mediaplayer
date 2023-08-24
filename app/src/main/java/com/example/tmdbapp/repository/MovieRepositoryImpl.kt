package com.example.tmdbapp.repository

import android.util.Log
import com.example.tmdbapp.helper.RetrofitHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.service.MovieService

class MovieRepositoryImpl : MovieRepository {

    private val TAG = "Movie Repository"
    private val movieService: MovieService = RetrofitHelper().movieService

    override suspend fun getTopRatedMovies(): List<Movie>? {
        try {
            val response = movieService.getTopRatedMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return responseBody?.movies ?: emptyList()
            } else {
                throw Exception("Failed to fetch top rated movies")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getNowPlayingMovies(): List<Movie>? {
        try {
            val response = movieService.getNowPlayingMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return responseBody?.movies ?: emptyList()

            } else {
                throw Exception("Failed to fetch now playing movies")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getPopularMovies(): List<Movie>? {
        try {
            val response = movieService.getPopularMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return responseBody?.movies ?: emptyList()
            } else {
                throw Exception("Failed to fetch popular movies")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getUpcomingMovies(): List<Movie>? {
        try {
            val response = movieService.getUpcomingMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return responseBody?.movies ?: emptyList()

            } else {
                throw Exception("Failed to fetch upcoming movies")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching upcoming movies", e)
        }
        return null
    }

    override suspend fun getMovieDetail(id: Int): MovieDetails? {
        try {
            val response = movieService.getMovieDetail(id)
            if (response.isSuccessful) {
                return response.body()
            } else {
                throw Exception("Failed to fetch movies details")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getCast(id: Int): List<Cast>? {

        try {
            val response = movieService.getMovieCredits(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                return responseBody?.cast
            } else {
                throw Exception("Failed to fetch movie's cast")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getSimilarMovies(id: Int): List<Movie>? {
        try {
            val response = movieService.getSimilarMovies(id)
            if (response.isSuccessful) {
                return response.body()?.movies
            } else {
                throw Exception("Failed to fetch similar movies")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}