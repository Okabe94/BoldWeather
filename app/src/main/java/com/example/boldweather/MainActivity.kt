package com.example.boldweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.boldweather.common.presentation.ForecastDetailScreen
import com.example.boldweather.common.presentation.SearchScreen
import com.example.boldweather.feature_search_suggestion.presentation.SearchSuggestionEvent
import com.example.boldweather.feature_weather_detail.presentation.ForecastDetailEvent
import com.example.boldweather.ui.theme.BoldWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoldWeatherTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = SearchScreen
                ) {
                    composable<SearchScreen> {
                        val viewModel = viewModel {
                            WeatherApplication.appModule.searchSuggestionViewModel
                        }
                        val state by viewModel.viewState.collectAsState()
                        Column(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = state.query, onValueChange = {
                                viewModel.onEvent(SearchSuggestionEvent.OnQueryChange(it))
                            })
                            when {
                                state.isLoading -> Text(text = "Loading...")
                                state.showSearchHint -> Text(text = "Search for something")
                                state.showEmptyState -> Text(text = "Loading...")
                                state.suggestions.isNotEmpty() -> {
                                    LazyColumn {
                                        items(state.suggestions) {
                                            Text(text = it.name,
                                                 color = Color.Black,
                                                 modifier = Modifier
                                                     .padding(16.dp)
                                                     .clickable {
                                                         navController.navigate(
                                                             ForecastDetailScreen(
                                                                 name = it.name,
                                                                 region = it.region,
                                                                 country = it.country
                                                             )
                                                         )
                                                     }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    composable<ForecastDetailScreen> {
                        val args = it.toRoute<ForecastDetailScreen>()
                        val viewModel = viewModel { WeatherApplication.appModule.forecastViewModel }
                        val state by viewModel.viewState.collectAsState()
                        LaunchedEffect(args.name, args.region, args.country) {
                            viewModel.onEvent(
                                ForecastDetailEvent.OnLoadForecast(
                                    name = args.name,
                                    region = args.region,
                                    country = args.country
                                )
                            )
                        }
                        Column(modifier = Modifier.fillMaxSize()) {
                            state.forecast?.let { forecast ->
                                Text(text = forecast.name)
                                Text(text = forecast.region)
                                Text(text = forecast.country)
                                Text(text = forecast.localtime)
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(forecast.forecastList) { future ->
                                        Card {
                                            Text(text = future.date)
                                            Text(text = future.avgTempC.toString())
                                            Text(text = future.condition.text)
                                            Text(text = future.condition.code.toString())
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}