package com.example.boldweather.feature_weather_detail.presentation

sealed class ForecastDetailEvent {
    data class OnLoadForecast(
        val name: String,
        val region: String,
        val country: String
    ) : ForecastDetailEvent()
}