package com.example.tmdbapp.helper

import com.example.tmdbapp.interceptor.MovieApiInterceptor
import com.example.tmdbapp.service.MovieService
import com.example.tmdbapp.service.PersonService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper{
val movieService = retrofit.create(MovieService::class.java)
val personService = retrofit.create(PersonService::class.java)
 companion object{
     const val BASE_URL = "https://api.themoviedb.org/3/"
     const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original/"
     const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
     const val API_KEY = "209ab1e1718e3d40b785ab11b02edc50"
     private val okHttpClient = OkHttpClient.Builder().addInterceptor(MovieApiInterceptor()).build()
     private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
         .addConverterFactory(GsonConverterFactory.create()).build()

 }
}