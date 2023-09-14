package com.example.tmdbapp.extensions

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tmdbapp.helper.NetworkHelper
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

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

fun View.gone(){
    this.visibility=View.GONE
}

fun View.visible(){
    this.visibility=View.VISIBLE



}

fun Context.noInternetSnackbar(view: View,context:Context, loadData:()->Unit){
    val snackbar = Snackbar.make(
        view,
        "No internet connection",
        Snackbar.LENGTH_INDEFINITE
    )

    snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onShown(transientBottomBar: Snackbar?) {
            super.onShown(transientBottomBar)
            transientBottomBar?.view?.findViewById<Button>(com.google.android.material.R.id.snackbar_action)
                ?.setOnClickListener {
                    if (NetworkHelper.isInternetConnected(context)) {
                        Log.d("testing home frag", "inside snackbar action")
                        loadData.invoke()
                        snackbar.dismiss()
                    }
                }

        }
    })
    snackbar.setAction(
        "Retry"
    ) {

    }
    snackbar.show()
}