package com.example.boldweather.data.network

import com.example.boldweather.data.model.ForecastResponse
import com.example.boldweather.data.model.SearchSuggestionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("search.json")
    suspend fun getSearchSuggestions(
        @Query("q") query: String,
        @Query("key") apiKey: String,
    ): List<SearchSuggestionResponse>

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("days") days: Int,
        @Query("aqi") airQuality: String,
        @Query("alerts") alerts: String,
        @Query("key") apiKey: String,
    ): ForecastResponse
}