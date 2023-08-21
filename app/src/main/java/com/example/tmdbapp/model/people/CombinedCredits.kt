package com.example.tmdbapp.model.people

data class CombinedCredits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)