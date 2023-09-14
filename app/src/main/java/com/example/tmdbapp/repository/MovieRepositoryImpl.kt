package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.videos.Video
import com.example.tmdbapp.service.MovieService

class MovieRepositoryImpl : MovieRepository {
    private val movieService: MovieService = NetworkHelper.movieService

    override suspend fun getTopRatedMovies(): ResponseHelper<List<Movie>> {
        return try {
            val response = movieService.getTopRatedMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.movies)
            } else {
                ResponseHelper.Error("Failed to fetch top rated movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getNowPlayingMovies(): ResponseHelper<List<Movie>>  {
        return try {
            val response = movieService.getNowPlayingMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.movies)
            } else {
                ResponseHelper.Error("Failed to fetch now playing movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getPopularMovies(): ResponseHelper<List<Movie>> {
        return try {
            val response = movieService.getPopularMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.movies)
            } else {
                ResponseHelper.Error("Failed to fetch popular movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getUpcomingMovies(): ResponseHelper<List<Movie>> {
        return try {
            val response = movieService.getUpcomingMovies()
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.movies)
            } else {
                ResponseHelper.Error("Failed to fetch upcoming movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getMovieDetail(id: Int): ResponseHelper<MovieDetails> {
        return try {
            val response = movieService.getMovieDetail(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody)
            } else {
                ResponseHelper.Error("Failed to fetch popular movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getCast(id: Int): ResponseHelper<List<Cast>> {

        return try {
            val response = movieService.getMovieCredits(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.cast)
            } else {
                ResponseHelper.Error("Failed to fetch popular movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getSimilarMovies(id: Int): ResponseHelper<List<Movie>> {
        return try {
            val response = movieService.getSimilarMovies(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.movies)
            } else {
                ResponseHelper.Error("Failed to fetch popular movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }

    override suspend fun getMovieVideos(id: Int): ResponseHelper<List<Video>> {
        return try {
            val response = movieService.getMovieVideos(id)
            if (response.isSuccessful) {
                val responseBody = response.body()
                ResponseHelper.Success(responseBody?.videoList)
            } else {
                ResponseHelper.Error("Failed to fetch popular movies")
            }
        } catch (e: Exception) {
            ResponseHelper.Error(e.message.toString())
        }
    }
}