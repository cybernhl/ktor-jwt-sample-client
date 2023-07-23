package com.example.ktor_jwt_demo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JwtApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}