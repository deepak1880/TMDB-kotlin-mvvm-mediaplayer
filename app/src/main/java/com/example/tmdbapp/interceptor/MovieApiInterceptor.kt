package com.example.tmdbapp.interceptor

import com.example.tmdbapp.helper.RetrofitHelper
import okhttp3.Interceptor
import okhttp3.Response

class MovieApiInterceptor : Interceptor {
    private val API_KEY_TAG = "api_key"
    private val apiKey = RetrofitHelper.API_KEY
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest
            .url
            .newBuilder()
            .addQueryParameter(API_KEY_TAG, apiKey)
            .build()

        val requestBuilder = originalRequest
            .newBuilder()
            .url(newRequest)
            .build()

        return chain.proceed(requestBuilder)
    }
}