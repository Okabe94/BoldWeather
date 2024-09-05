package com.example.boldweather.data.model

import com.squareup.moshi.Json

data class ForecastResponse(
    @Json(name = "current") val currentWeather: Current,
    @Json(name = "forecast") val forecast: Forecast,
    @Json(name = "location") val location: Location
)

data class Current(@Json(name = "condition") val condition: Condition)

data class Forecast(
    @Json(name = "forecastday") val forecastList: List<ForecastDay>
)

data class Location(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String,
    @Json(name = "localtime") val localtime: String,
)

data class Condition(
    @Json(name = "text") val text: String,
    @Json(name = "code") val code: Int,
)

data class ForecastDay(
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: Current,
    @Json(name = "hour") val hour: List<Hour>
)

data class Hour(
    @Json(name = "condition") val condition: Condition,
    @Json(name = "time") val time: String,
)