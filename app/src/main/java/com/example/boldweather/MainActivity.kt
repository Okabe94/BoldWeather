package com.example.boldweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.boldweather.data.network.WeatherApi
import com.example.boldweather.ui.theme.BoldWeatherTheme

class A(val api: WeatherApi)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a = A(WeatherApplication.appModule.weatherApi)
        enableEdgeToEdge()
        setContent {
            BoldWeatherTheme {
                Text(text = a.api.toString())
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