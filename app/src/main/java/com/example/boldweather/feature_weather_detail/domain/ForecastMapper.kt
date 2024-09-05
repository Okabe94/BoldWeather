package com.example.boldweather.feature_weather_detail.domain

import com.example.boldweather.feature_weather_detail.data.ForecastApiResponse
import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO

interface ForecastMapper {
    fun mapForecast(response: ForecastApiResponse): ForecastDTO
}