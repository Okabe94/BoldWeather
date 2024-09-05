package com.example.boldweather.feature_weather_detail.presentation

import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO

data class ForecastDetailViewState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val forecast: ForecastDTO? = null
)