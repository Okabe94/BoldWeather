package com.example.boldweather.feature_search_suggestion.data

import com.example.boldweather.BuildConfig
import com.example.boldweather.common.data.network.WeatherApi
import com.example.boldweather.feature_search_suggestion.domain.SearchSuggestionMapper
import com.example.boldweather.feature_search_suggestion.domain.SearchSuggestionRepository
import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO

class SearchSuggestionRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val searchSuggestionMapper: SearchSuggestionMapper
) : SearchSuggestionRepository {

    override suspend fun getSearchSuggestions(
        query: String,
    ): List<SearchSuggestionDTO> {
        val response = weatherApi.getSearchSuggestions(query, BuildConfig.WEATHER_API_KEY)
        return searchSuggestionMapper.mapSearchSuggestion(response)
    }
}