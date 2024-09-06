package com.example.boldweather.feature_weather_detail.data

import com.example.boldweather.BuildConfig
import com.example.boldweather.common.data.network.WeatherApi
import com.example.boldweather.feature_weather_detail.domain.ForecastMapper
import com.example.boldweather.feature_weather_detail.domain.ForecastRepository
import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO

private const val DEFAULT_SEARCH_PREFIX = "id:"
private const val DEFAULT_DAYS = 3
private const val DEFAULT_ALERTS = "no"
private const val DEFAULT_AQI = "no"

class ForecastRepositoryImpl(
    private val weatherApi: WeatherApi, private val mapper: ForecastMapper
) : ForecastRepository {

    override suspend fun getForecast(id: Int): ForecastDTO {
        val query = "$DEFAULT_SEARCH_PREFIX$id"
        val response = weatherApi.getForecast(
            query = query,
            days = DEFAULT_DAYS,
            airQuality = DEFAULT_AQI,
            alerts = DEFAULT_ALERTS,
            apiKey = BuildConfig.WEATHER_API_KEY
        )
        return mapper.mapForecast(response)
    }

}