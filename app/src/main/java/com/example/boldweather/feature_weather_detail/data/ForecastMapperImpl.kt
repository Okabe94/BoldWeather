package com.example.boldweather.feature_weather_detail.data

import com.example.boldweather.feature_weather_detail.domain.ForecastMapper
import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO
import com.example.boldweather.feature_weather_detail.domain.model.ForecastItem
import com.example.boldweather.feature_weather_detail.domain.model.WeatherCondition
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private const val ICON_PREFIX = "https:"
private const val MAX_FUTURE_AMOUNT = 2

class ForecastMapperImpl : ForecastMapper {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun mapForecast(response: ForecastApiResponse): ForecastDTO = with(response) {
        return ForecastDTO(
            currentWeatherTemp = current?.tempC ?: 0.0,
            currentWeatherCondition = mapCondition(current?.condition),
            name = location?.name.orEmpty(),
            region = location?.region.orEmpty(),
            country = location?.country.orEmpty(),
            localtime = mapTime(time = location?.localtime, fullName = true),
            forecastList = forecast?.forecastDayList
                ?.map { forecastDay ->
                    ForecastItem(
                        date = mapTime(time = forecastDay.date, fullName = false),
                        avgTempC = forecastDay.day?.avgTempC ?: 0.0,
                        condition = mapCondition(forecastDay.day?.condition)
                    )
                }
                .orEmpty()
                .takeLast(MAX_FUTURE_AMOUNT)
        )
    }

    private fun mapTime(time: String?, fullName: Boolean): String {
        if (time == null) return ""

        // Extract only the date portion, remove the time part
        val toFormat = time.substringBefore(" ")
        val date = LocalDate.parse(toFormat, dateFormatter)


        return date.run {
            val textStyle = if (fullName) TextStyle.FULL else TextStyle.SHORT
            val locale = Locale.getDefault()
            val dayName = dayOfWeek.getDisplayName(textStyle, locale)
                .replaceFirstChar { it.uppercase() }

            val monthName = month.getDisplayName(textStyle, locale)

            if (fullName) {
                "$dayName $dayOfMonth $monthName, $year"
            } else {
                "$dayName $dayOfMonth"
            }
        }
    }

    private fun mapCondition(condition: Condition?): WeatherCondition {
        val icon = condition?.icon.orEmpty()
        val iconURL =
            if (icon.isEmpty()) icon
            else "$ICON_PREFIX$icon"

        return WeatherCondition(icon = iconURL, text = condition?.text.orEmpty())
    }
}