package com.example.boldweather.common.data.network

import com.example.boldweather.feature_weather_detail.data.ForecastApiResponse
import com.example.boldweather.feature_search_suggestion.data.SearchSuggestionApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("search.json")
    suspend fun getSearchSuggestions(
        @Query("q") query: String,
        @Query("key") apiKey: String,
    ): List<SearchSuggestionApiResponse>

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("days") days: Int,
        @Query("aqi") airQuality: String,
        @Query("alerts") alerts: String,
        @Query("key") apiKey: String,
    ): ForecastApiResponse
}