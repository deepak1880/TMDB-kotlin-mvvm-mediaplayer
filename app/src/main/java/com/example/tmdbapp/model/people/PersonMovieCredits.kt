package com.example.tmdbapp.model.people

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonMovieCredits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
) : Parcelable