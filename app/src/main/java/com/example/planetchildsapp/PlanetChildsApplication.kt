package com.example.planetchildsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application класс для инициализации Hilt dependency injection
 */
@HiltAndroidApp
class PlanetChildsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Здесь можно добавить дополнительную инициализацию приложения
    }
}
