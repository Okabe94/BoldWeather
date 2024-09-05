package com.example.boldweather.di

import com.example.boldweather.common.data.network.WeatherApi
import com.example.boldweather.feature_search_suggestion.data.SearchSuggestionMapperImpl
import com.example.boldweather.feature_search_suggestion.data.SearchSuggestionRepositoryImpl
import com.example.boldweather.feature_search_suggestion.domain.SearchSuggestionMapper
import com.example.boldweather.feature_search_suggestion.domain.SearchSuggestionRepository
import com.example.boldweather.feature_search_suggestion.presentation.SearchSuggestionViewModel
import com.example.boldweather.feature_weather_detail.data.ForecastMapperImpl
import com.example.boldweather.feature_weather_detail.data.ForecastRepositoryImpl
import com.example.boldweather.feature_weather_detail.domain.ForecastMapper
import com.example.boldweather.feature_weather_detail.domain.ForecastRepository
import com.example.boldweather.feature_weather_detail.presentation.ForecastViewModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface ApplicationModule {
    val searchSuggestionViewModel: SearchSuggestionViewModel
    val forecastViewModel: ForecastViewModel
}

class WeatherApplicationModule : ApplicationModule {

    private val dispatchers: AppDispatcher by lazy { WeatherAppDispatcher() }

    private val forecastMapper: ForecastMapper by lazy { ForecastMapperImpl() }

    private val searchSuggestionMapper: SearchSuggestionMapper by lazy {
        SearchSuggestionMapperImpl()
    }

    private val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    private val forecastRepository: ForecastRepository by lazy {
        ForecastRepositoryImpl(
            weatherApi = weatherApi,
            mapper = forecastMapper
        )
    }

    private val searchSuggestionRepository: SearchSuggestionRepository by lazy {
        SearchSuggestionRepositoryImpl(
            weatherApi = weatherApi,
            searchSuggestionMapper = searchSuggestionMapper
        )
    }

    override val searchSuggestionViewModel: SearchSuggestionViewModel by lazy {
        SearchSuggestionViewModel(
            searchRepository = searchSuggestionRepository,
            dispatchers = dispatchers
        )
    }

    // Done this way because coroutine scope is cleared after the view model is destroyed
    // Meaning no more calls can be made in the scope
    override val forecastViewModel: ForecastViewModel
        get() = ForecastViewModel(
            forecastRepository = forecastRepository,
            dispatchers = dispatchers
        )

}
