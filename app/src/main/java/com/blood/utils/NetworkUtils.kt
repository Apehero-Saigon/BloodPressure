package com.blood.utils

import android.content.Context
import android.net.ConnectivityManager
import com.blood.App

class NetworkUtils {

    companion object {
        var app: App = App.app

        fun isInternetAvailable(): Boolean {
            val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm?.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }
    }
}