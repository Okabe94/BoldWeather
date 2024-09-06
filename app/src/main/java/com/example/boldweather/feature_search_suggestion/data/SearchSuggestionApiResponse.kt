package com.example.boldweather.feature_search_suggestion.data

import com.squareup.moshi.Json

data class SearchSuggestionApiResponse(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "region") val region: String?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "lat") val lat: Double?,
    @field:Json(name = "lon") val lon: Double?
)