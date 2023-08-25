package com.example.tmdbapp.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.tmdbapp.interceptor.MovieApiInterceptor
import com.example.tmdbapp.service.MovieService
import com.example.tmdbapp.service.PersonService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkHelper {
    private val retrofit: Retrofit
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original/"
    const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
    const val API_KEY = "209ab1e1718e3d40b785ab11b02edc50"
    val movieService: MovieService
    val personService: PersonService
    init {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(MovieApiInterceptor()).build()
        retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

        movieService = retrofit.create(MovieService::class.java)
        personService = retrofit.create(PersonService::class.java)
    }
    fun isInternetConnected(context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?:return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

    }
}