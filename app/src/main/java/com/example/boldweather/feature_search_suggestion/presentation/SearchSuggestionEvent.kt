package com.example.boldweather.feature_search_suggestion.presentation

import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO

sealed interface SearchSuggestionEvent {
    data class OnQueryChange(val query: String): SearchSuggestionEvent
    data class OnSuggestionClick(val suggestion: SearchSuggestionDTO): SearchSuggestionEvent
}