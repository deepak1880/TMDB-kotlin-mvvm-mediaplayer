package com.example.tmdbapp.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

enum class FragmentHelper {
    REPLACE,
    ADD
}

fun FragmentManager.performFragmentTransaction(
    containerId: Int,
    fragment : Fragment,
    fragmentHelper: FragmentHelper,
    addToBackStack: Boolean = true
) {
    val ft = beginTransaction()
    when(fragmentHelper){
        FragmentHelper.ADD -> ft.add(containerId,fragment)
        FragmentHelper.REPLACE -> ft.replace(containerId,fragment)
    }

    if(addToBackStack) ft.addToBackStack(null)
    ft.commit()
}