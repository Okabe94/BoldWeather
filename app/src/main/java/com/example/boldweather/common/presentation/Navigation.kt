package com.example.boldweather.common.presentation

import kotlinx.serialization.Serializable

@Serializable
object SearchScreen

@Serializable
data class ForecastDetailScreen(
    val name: String,
    val region: String,
    val country: String
)