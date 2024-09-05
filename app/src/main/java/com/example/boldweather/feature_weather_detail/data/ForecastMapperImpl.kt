package com.example.boldweather.feature_weather_detail.data

import com.example.boldweather.feature_weather_detail.domain.ForecastMapper
import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO
import com.example.boldweather.feature_weather_detail.domain.model.ForecastItem
import com.example.boldweather.feature_weather_detail.domain.model.WeatherCondition

class ForecastMapperImpl : ForecastMapper {

    override fun mapForecast(response: ForecastApiResponse): ForecastDTO = with(response) {
        return ForecastDTO(
            currentWeatherTemp = current?.tempC ?: 0.0,
            currentWeatherCondition = mapCondition(current?.condition),
            lat = location?.lat ?: 0.0,
            lon = location?.lon ?: 0.0,
            name = location?.name.orEmpty(),
            region = location?.region.orEmpty(),
            country = location?.country.orEmpty(),
            localtime = location?.localtime.orEmpty(),
            forecastList = forecast?.forecastList?.map { forecastDay ->
                ForecastItem(
                    date = forecastDay.date.orEmpty(),
                    avgTempC = forecastDay.day?.avgTempC ?: 0.0,
                    condition = mapCondition(forecastDay.day?.condition)
                )
            }.orEmpty()
        )
    }

    private fun mapCondition(condition: Condition?): WeatherCondition =
        WeatherCondition(text = condition?.text.orEmpty(), code = condition?.code ?: 0)
}