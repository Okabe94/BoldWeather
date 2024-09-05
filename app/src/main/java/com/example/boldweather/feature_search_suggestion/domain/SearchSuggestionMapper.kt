package com.example.boldweather.feature_search_suggestion.domain

import com.example.boldweather.feature_search_suggestion.data.SearchSuggestionApiResponse
import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO

interface SearchSuggestionMapper {
    fun mapSearchSuggestion(response: List<SearchSuggestionApiResponse>): List<SearchSuggestionDTO>
}