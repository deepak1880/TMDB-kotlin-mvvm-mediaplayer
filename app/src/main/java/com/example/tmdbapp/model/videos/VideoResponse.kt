package com.example.tmdbapp.model.videos

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val videoList: List<Video>
)