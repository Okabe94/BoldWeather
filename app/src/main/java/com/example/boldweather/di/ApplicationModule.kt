package com.example.boldweather.di

import com.example.boldweather.data.network.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface ApplicationModule {
    val weatherApi: WeatherApi
}

class WeatherApplicationModule : ApplicationModule {
    override val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}
