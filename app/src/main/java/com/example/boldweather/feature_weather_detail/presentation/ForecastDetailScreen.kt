package com.example.boldweather.feature_weather_detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.boldweather.R
import com.example.boldweather.common.presentation.DefaultCardComposable
import com.example.boldweather.common.presentation.LoadingStateComposable
import com.example.boldweather.common.presentation.ScreenBackground
import com.example.boldweather.feature_weather_detail.domain.model.ForecastDTO
import com.example.boldweather.feature_weather_detail.domain.model.ForecastItem
import com.example.boldweather.feature_weather_detail.domain.model.WeatherCondition
import com.example.boldweather.ui.theme.BoldWeatherTheme

@Composable
fun ForecastDetailRoot(viewModel: ForecastViewModel) {
    val state by viewModel.viewState.collectAsState()
    ForecastDetailScreen(state = state)
}

@Composable
fun ForecastDetailScreen(state: ForecastDetailViewState) {
    ScreenBackground(modifier = Modifier.padding(16.dp)) {
        when {
            state.isLoading -> LoadingStateComposable()
            state.isError -> ErrorStateComposable()
            state.forecast == null -> EmptyStateComposable()
            else -> SuccessfulState(state)
        }
    }
}

@Composable
private fun EmptyStateComposable() {
    DefaultCardComposable(
        title = stringResource(R.string.forecast_detail_empty_title),
        message = stringResource(R.string.forecast_detail_empty_message),
        icon = Icons.Outlined.Done
    )
}

@Composable
private fun ErrorStateComposable() {
    DefaultCardComposable(
        title = stringResource(R.string.forecast_detail_error_title),
        message = stringResource(R.string.forecast_detail_error_message),
        icon = Icons.Outlined.Warning
    )
}

@Composable
private fun SuccessfulState(state: ForecastDetailViewState) {
    state.forecast?.let { forecast ->
        HeaderSection(forecast)
        Spacer(modifier = Modifier.height(8.dp))

        MainForecastCard(forecast)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.next_days_forecast),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            items(forecast.forecastList) { future ->
                FutureForecastItem(future)
            }
        }
    }
}

@Composable
private fun HeaderSection(forecast: ForecastDTO) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "${forecast.region}, ${forecast.country}",
        style = MaterialTheme.typography.bodyMedium
    )

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = forecast.name,
        style = MaterialTheme.typography.displayLarge,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun MainForecastCard(forecast: ForecastDTO) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AsyncImage(
                    modifier = Modifier.size(100.dp),
                    model = forecast.currentWeatherCondition.icon,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${forecast.currentWeatherTemp}°",
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = forecast.currentWeatherCondition.text,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = forecast.localtime,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun FutureForecastItem(future: ForecastItem) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            )
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = future.date,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        AsyncImage(
            modifier = Modifier.size(48.dp),
            model = future.condition.icon,
            contentDescription = null,
        )
        Text(
            text = future.condition.text,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = "${future.avgTempC}°",
            fontFamily = FontFamily.Default,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@PreviewLightDark
@Composable
private fun ForecastDetailScreenPreview() {
    val defaultCondition = WeatherCondition(
        icon = "https://cdn.weatherapi.com/weather/64x64/night/116.png",
        text = "Partly cloudy"
    )

    BoldWeatherTheme {
        ForecastDetailScreen(
            state = ForecastDetailViewState(
                isLoading = false,
                isError = false,
                ForecastDTO(
                    currentWeatherTemp = 22.0,
                    currentWeatherCondition = defaultCondition,
                    name = "Name",
                    region = "Region",
                    country = "Country",
                    localtime = "Localtime",
                    forecastList = listOf(
                        ForecastItem(
                            date = "DATE1",
                            avgTempC = 20.0,
                            condition = defaultCondition
                        ),

                        ForecastItem(
                            date = "DATE2",
                            avgTempC = 17.0,
                            condition = defaultCondition
                        ),
                        ForecastItem(
                            date = "DATE3",
                            avgTempC = 2.0,
                            condition = defaultCondition
                        )
                    )
                )
            )
        )
    }
}