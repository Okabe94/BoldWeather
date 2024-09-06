package com.example.boldweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boldweather.feature_search_suggestion.presentation.SearchSuggestionRoot
import com.example.boldweather.feature_weather_detail.presentation.ForecastDetailEvent
import com.example.boldweather.feature_weather_detail.presentation.ForecastDetailRoot
import com.example.boldweather.ui.theme.BoldWeatherTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoldWeatherTheme {
                val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigableListDetailPaneScaffold(
                        modifier = Modifier.padding(innerPadding),
                        navigator = navigator,
                        listPane = { ListScreen(navigator = navigator) },
                        detailPane = { DetailScreen(navigator = navigator) },
                        defaultBackBehavior = BackNavigationBehavior.PopUntilCurrentDestinationChange,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ListScreen(navigator: ThreePaneScaffoldNavigator<Any>) {
    val viewModel = viewModel { WeatherApplication.appModule.searchSuggestionViewModel }
    SearchSuggestionRoot(navigator = navigator, viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun DetailScreen(navigator: ThreePaneScaffoldNavigator<Any>) {
    val args = navigator.currentDestination?.content?.toString()?.toInt()
    val viewModel = viewModel { WeatherApplication.appModule.forecastViewModel }

    if (args != null) {
        LaunchedEffect(args) {
            viewModel.onEvent(ForecastDetailEvent.OnLoadForecast(args))
        }

        ForecastDetailRoot(viewModel = viewModel)
    }
}

