package com.example.boldweather

import android.app.Application
import com.example.boldweather.di.ApplicationModule
import com.example.boldweather.di.WeatherApplicationModule

class WeatherApplication : Application(){

    companion object{
        lateinit var appModule: ApplicationModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = WeatherApplicationModule()
    }
}