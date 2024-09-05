package com.example.boldweather.feature_search_suggestion.data

import com.squareup.moshi.Json

data class SearchSuggestionApiResponse(
    @Json(name = "name") val name: String?,
    @Json(name = "region") val region: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "lat") val lat: Double?,
    @Json(name = "lon") val lon: Double?
)