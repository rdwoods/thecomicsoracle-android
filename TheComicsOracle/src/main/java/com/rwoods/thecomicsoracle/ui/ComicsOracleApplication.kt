package com.rwoods.thecomicsoracle.ui

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.firebase.FirebaseApp

/**
 * Created by rwoods on 2/23/2016.
 */
class ComicsOracleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}