package com.example.boldweather.feature_weather_detail.domain

import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO

interface ForecastRepository {
    suspend fun getForecast(
        query: String,
        days: Int,
        aqi: String,
        alerts: String,
        key: String
    ): ForecastDTO
}