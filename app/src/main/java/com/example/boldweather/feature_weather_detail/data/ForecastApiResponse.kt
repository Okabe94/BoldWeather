package com.example.boldweather.feature_weather_detail.data

import com.squareup.moshi.Json

data class ForecastApiResponse(
    @Json(name = "current") val current: CurrentWeather?,
    @Json(name = "forecast") val forecast: Forecast?,
    @Json(name = "location") val location: Location?
)

data class CurrentWeather(
    @Json(name = "temp_c") val tempC: Double?,
    @Json(name = "condition") val condition: Condition?
)

data class Forecast(
    @Json(name = "forecastday") val forecastList: List<ForecastDay>?
)

data class Location(
    @Json(name = "lat") val lat: Double?,
    @Json(name = "lon") val lon: Double?,
    @Json(name = "name") val name: String?,
    @Json(name = "region") val region: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "localtime") val localtime: String?,
)

data class ForecastDay(
    @Json(name = "date") val date: String?,
    @Json(name = "day") val day: Day?,
)

data class Day(
    @Json(name = "avgtemp_c") val avgTempC: Double?,
    @Json(name = "condition") val condition: Condition?
)

data class Condition(
    @Json(name = "text") val text: String?,
    @Json(name = "code") val code: Int?,
)

