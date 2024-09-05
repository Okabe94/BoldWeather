package com.example.boldweather.feature_weather_detail.domain.model

data class ForecastDTO(
    val currentWeatherTemp: Double,
    val currentWeatherCondition: WeatherCondition,
    val lat: Double,
    val lon: Double,
    val name: String,
    val region: String,
    val country: String,
    val localtime: String,
    val forecastList: List<ForecastItem>
)

data class WeatherCondition(
    val text: String,
    val code: Int
)

data class ForecastItem(
    val date: String,
    val avgTempC: Double,
    val condition: WeatherCondition
)
