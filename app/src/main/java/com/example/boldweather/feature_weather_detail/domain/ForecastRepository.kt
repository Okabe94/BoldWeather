package com.example.boldweather.feature_weather_detail.domain

import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO

interface ForecastRepository {
    suspend fun getForecast(
        name: String,
        region: String,
        country: String
    ): ForecastDTO
}