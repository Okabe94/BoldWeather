package com.example.boldweather.feature_weather_detail.presentation

sealed class ForecastDetailEvent {
    data class OnLoadForecast(val id: Int) : ForecastDetailEvent()
}