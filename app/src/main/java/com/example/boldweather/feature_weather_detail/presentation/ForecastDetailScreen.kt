package com.example.boldweather.feature_weather_detail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ForecastDetailRoot(viewModel: ForecastViewModel) {
    val state by viewModel.viewState.collectAsState()
    ForecastDetailScreen(
        onEvent = viewModel::onEvent,
        state = state,
    )
}

@Composable
private fun ForecastDetailScreen(
    onEvent: (ForecastDetailEvent) -> Unit,
    state: ForecastDetailViewState,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        state.forecast?.let { forecast ->
            Text(text = forecast.name)
            Text(text = forecast.region)
            Text(text = forecast.country)
            Text(text = forecast.localtime)
            AsyncImage(
                model = forecast.currentWeatherCondition.icon,
                contentDescription = null,
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(forecast.forecastList) { future ->
                    Card {
                        Text(text = future.date)
                        Text(text = future.avgTempC.toString())
                        AsyncImage(
                            model = future.condition.icon,
                            contentDescription = null,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}