package com.example.boldweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boldweather.feature_search_suggestion.presentation.SearchSuggestionEvent
import com.example.boldweather.ui.theme.BoldWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel { WeatherApplication.appModule.searchSuggestionViewModel }

            val state by viewModel.viewState.collectAsState()
            BoldWeatherTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.query,
                        onValueChange = {
                            viewModel.onEvent(SearchSuggestionEvent.OnQueryChange(it))
                        }
                    )
                    if (state.isLoading) {
                        Text(text = "Loading...")
                        return@BoldWeatherTheme
                    }
                    if (state.showSearchHint) {
                        Text(text = "Search something")
                        return@BoldWeatherTheme
                    }
                    if (state.showEmptyState) {
                        Text(text = "No results found")
                        return@BoldWeatherTheme
                    }
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(state.suggestions) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Blue, RoundedCornerShape(4.dp))
                            ) {
                                Text(text = it.name)
                                Text(text = it.region)
                                Text(text = it.country)
                                Text(text = it.lat.toString())
                                Text(text = it.lon.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true) @Composable fun GreetingPreview() {
    BoldWeatherTheme {
        Greeting("Android")
    }
}