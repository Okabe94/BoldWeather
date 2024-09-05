package com.example.boldweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.boldweather.data.network.WeatherApi
import com.example.boldweather.ui.theme.BoldWeatherTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val server = Retrofit.Builder().baseUrl(
            "https://api.weatherapi.com/v1/"
        ).addConverterFactory(MoshiConverterFactory.create())
            .build()
        setContent {
            val scope = rememberCoroutineScope()
            LaunchedEffect(true) {
                scope.launch {
                    val api = server.create(WeatherApi::class.java)
                    val response =
                        api.getSearchSuggestions(
                            query = "Buenos Aires",
                            apiKey = "de5553176da64306b86153651221606"
                        )
                    response.forEach { println(it.toString()) }
                }
            }
            BoldWeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BoldWeatherTheme {
        Greeting("Android")
    }
}