package com.example.tmdbapp.helper

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

object CalculationHelper {
    fun findAge(birthday: String?) : Int? {
        var age : Int? = null
        birthday?.let{
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
            val birthDate = LocalDate.parse(it, dateFormatter)
            val currentDate = LocalDate.now()
            val period = Period.between(birthDate, currentDate)
            age = period.years
        }
        return age
    }
}