package com.example.tmdbapp.model.recyclerviews;

import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.videos.Video

data class MovieDetailsRecyclerView(
    val imageUrl: String,
    val title: String,
    val description : String,
    val castList: List<Cast>,
    val movieDetailsList: List<Video>
)