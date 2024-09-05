package com.example.boldweather.feature_search_suggestion.domain

import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO

interface SearchSuggestionRepository {
    suspend fun getSearchSuggestions(query: String): List<SearchSuggestionDTO>
}