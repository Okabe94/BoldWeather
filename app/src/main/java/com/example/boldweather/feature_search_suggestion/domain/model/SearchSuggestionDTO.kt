package com.example.boldweather.feature_search_suggestion.domain.model

data class SearchSuggestionDTO(
    val id : Int,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double
)