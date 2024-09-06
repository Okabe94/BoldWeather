package com.example.boldweather.feature_weather_detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boldweather.di.AppDispatcher
import com.example.boldweather.feature_weather_detail.domain.ForecastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ForecastViewModel(
    private val forecastRepository: ForecastRepository,
    private val dispatchers: AppDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow(ForecastDetailViewState())
    val viewState = _viewState.asStateFlow()

    fun onEvent(event: ForecastDetailEvent) {
        when (event) {
            is ForecastDetailEvent.OnLoadForecast -> {
                viewModelScope.launch(dispatchers.default) {
                    _viewState.update { it.copy(isError = false, isLoading = true) }

                    try {
                        val forecast = forecastRepository.getForecast(id = event.id)
                        _viewState.update {
                            it.copy(isError = false, isLoading = false, forecast = forecast)
                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _viewState.update {
                            it.copy(isError = true, isLoading = false, forecast = null)
                        }
                    }
                }
            }
        }
    }
}
