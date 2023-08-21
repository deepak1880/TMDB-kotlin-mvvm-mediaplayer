package com.example.tmdbapp.helper

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalculationHelper {
    fun findAge(birthday: String) : Int {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = simpleDateFormat.parse(birthday)
        val currentDate = Calendar.getInstance()
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = date
        var age = currentDate.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if (currentDate.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }
}