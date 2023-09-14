package com.example.tmdbapp.helper

sealed class ResponseHelper<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T> : ResponseHelper<T>()
    class Success<T>(data: T? = null) : ResponseHelper<T>(data = data)
    class Error<T>(errorMessage: String) : ResponseHelper<T>(errorMessage = errorMessage)
}