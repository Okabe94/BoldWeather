package com.example.boldweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boldweather.BuildConfig
import com.example.boldweather.data.network.WeatherApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
   private val api: WeatherApi
) : ViewModel() {

    private val _viewState = MutableStateFlow("")
    val viewState = _viewState.asStateFlow()

    fun getWeather() {
        viewModelScope.launch {
            val response = api.getSearchSuggestions("Buenos Aires", BuildConfig.WEATHER_API_KEY)
            val names = response.joinToString(",") { it.name }
            _viewState.update { names }
        }
    }
}