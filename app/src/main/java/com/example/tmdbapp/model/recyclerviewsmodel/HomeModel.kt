package com.example.tmdbapp.model.recyclerviewsmodel;

import com.example.tmdbapp.helper.Status
import com.example.tmdbapp.model.Movie

data class HomeModel(
    val title: String,
    var movies: List<Movie>,
    var status: Status
) {
}
