package com.example.boldweather.data.model

import com.squareup.moshi.Json

data class SearchSuggestionResponse(
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double
)