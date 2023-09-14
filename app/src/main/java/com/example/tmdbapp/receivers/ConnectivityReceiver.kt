package com.example.tmdbapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tmdbapp.helper.NetworkHelper.isInternetConnected

class ConnectivityReceiver(private val callback: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            callback(isInternetConnected(it))
        }
    }
}