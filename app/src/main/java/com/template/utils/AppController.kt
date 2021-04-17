package com.template.utils

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("hvdfdvhj", "ujdschdfvb")
    }
}