package com.example.boldweather.feature_weather_detail.data

import com.example.boldweather.common.data.network.WeatherApi
import com.example.boldweather.feature_weather_detail.domain.ForecastMapper
import com.example.boldweather.feature_weather_detail.domain.ForecastRepository
import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO

class ForecastRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val mapper: ForecastMapper
) : ForecastRepository {

    override suspend fun getForecast(
        query: String,
        days: Int,
        aqi: String,
        alerts: String,
        key: String
    ): ForecastDTO {
        val response = weatherApi.getForecast(query, days, aqi, alerts, key)
        return mapper.mapForecast(response)
    }

}