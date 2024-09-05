package com.example.boldweather.feature_weather_detail.presentation

import androidx.lifecycle.ViewModel
import com.example.boldweather.di.AppDispatcher
import com.example.boldweather.feature_weather_detail.domain.ForecastRepository

class ForecastViewModel(
    private val forecastRepository: ForecastRepository,
    private val dispatchers: AppDispatcher
) : ViewModel() {

}