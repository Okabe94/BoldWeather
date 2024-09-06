package com.example.boldweather.feature_weather_detail.data

import com.squareup.moshi.Json

data class ForecastApiResponse(
    @field:Json(name = "current") val current: CurrentWeather?,
    @field:Json(name = "forecast") val forecast: Forecast?,
    @field:Json(name = "location") val location: Location?
)

data class CurrentWeather(
    @field:Json(name = "temp_c") val tempC: Double?,
    @field:Json(name = "condition") val condition: Condition?
)

data class Forecast(
    @field:Json(name = "forecastday") val forecastDayList: List<ForecastDay>?
)

data class Location(
    @field:Json(name = "lat") val lat: Double?,
    @field:Json(name = "lon") val lon: Double?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "region") val region: String?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "localtime") val localtime: String?,
)

data class ForecastDay(
    @field:Json(name = "date") val date: String?,
    @field:Json(name = "day") val day: Day?,
)

data class Day(
    @field:Json(name = "avgtemp_c") val avgTempC: Double?,
    @field:Json(name = "condition") val condition: Condition?
)

data class Condition(
    @field:Json(name = "icon") val icon: String?,
    @field:Json(name = "text") val text: String?
)

