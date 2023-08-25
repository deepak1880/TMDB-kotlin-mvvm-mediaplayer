package com.example.tmdbapp.model

import com.google.gson.annotations.SerializedName

data class Result(
    val title : String,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)