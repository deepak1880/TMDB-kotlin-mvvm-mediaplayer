package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.MovieDetails
import com.example.tmdbapp.model.Result
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.videos.Video
import com.example.tmdbapp.service.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepositoryImpl : BaseRepository(), MovieRepository {
    private val movieService: MovieService = NetworkHelper.movieService

    fun getData(): Flow<ResponseHelper<Result>> {
        return executeAPI {
            movieService.getPopularMovies()
        }
    }

    override fun getTopRatedMovies(): Flow<ResponseHelper<List<Movie>>> = flow {
        val result = movieService.getTopRatedMovies()
        if (result.isSuccessful) {
            emit(ResponseHelper.Success(result.body()?.movies ?: emptyList()))
        } else {
            emit(ResponseHelper.Error("Failed to fetch top-rated movies"))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(ResponseHelper.Error(it.message.toString()))
    }

    override fun getNowPlayingMovies(): Flow<ResponseHelper<List<Movie>>> = flow {
        val result = movieService.getNowPlayingMovies()
        if (result.isSuccessful) {
            emit(ResponseHelper.Success(result.body()?.movies ?: emptyList()))
        } else {
            emit(ResponseHelper.Error("Failed to fetch now playing movies"))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(ResponseHelper.Error(it.message.toString()))
    }


    override fun getPopularMovies(): Flow<ResponseHelper<List<Movie>>> = flow {
        val result = movieService.getPopularMovies()
        if (result.isSuccessful) {
            emit(ResponseHelper.Success(result.body()?.movies ?: emptyList()))
        } else {
            emit(ResponseHelper.Error("Failed to fetch popular movies"))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(ResponseHelper.Error(it.message.toString()))
    }

    override fun getUpcomingMovies(): Flow<ResponseHelper<List<Movie>>> = flow {
        val result = movieService.getUpcomingMovies()
        if (result.isSuccessful) {
            emit(ResponseHelper.Success(result.body()?.movies ?: emptyList()))
        } else {
            emit(ResponseHelper.Error("Failed to fetch top-rated movies"))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(ResponseHelper.Error(it.message.toString()))
    }

    override fun getMovieDetail(movieId: Int): Flow<ResponseHelper<MovieDetails>> = flow {
        val response = movieService.getMovieDetail(movieId)
        if (response.isSuccessful) {
            emit(ResponseHelper.Success(response.body()))
        } else {
            emit(ResponseHelper.Error("Failed to fetch movie details."))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(ResponseHelper.Error(it.message.toString()))
    }

    override fun getCast(movieId: Int): Flow<ResponseHelper<List<Cast>>> =
        flow {
            val response = movieService.getMovieCredits(movieId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                emit(ResponseHelper.Success(responseBody?.cast))
            } else {
                emit(ResponseHelper.Error("Failed to fetch cast of the movie."))
            }
        }.flowOn(Dispatchers.IO)
            .catch {
                emit(ResponseHelper.Error(it.message.toString()))
            }

    override fun getSimilarMovies(movieId: Int): Flow<ResponseHelper<List<Movie>>> =
        flow {
            val response = movieService.getSimilarMovies(movieId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                emit(ResponseHelper.Success(responseBody?.movies))
            } else {
                emit(ResponseHelper.Error("Failed to fetch popular movies"))
            }
        }.flowOn(Dispatchers.IO)
            .catch {
                emit(ResponseHelper.Error(it.message.toString()))
            }

    override fun getMovieVideos(movieId: Int): Flow<ResponseHelper<List<Video>>> = flow {
        val response = NetworkHelper.movieService.getMovieVideos(movieId)
        if (response.isSuccessful) {
            val responseBody = response.body()
            emit(ResponseHelper.Success(responseBody?.videoList))
        } else {
            emit(ResponseHelper.Error("Failed to fetch popular movies"))
        }
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResponseHelper.Error(it.message.toString()))
        }
}

