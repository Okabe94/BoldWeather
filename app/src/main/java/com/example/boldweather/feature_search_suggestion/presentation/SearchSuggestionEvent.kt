package com.example.boldweather.feature_search_suggestion.presentation

sealed interface SearchSuggestionEvent {
    data object OnClearQuery: SearchSuggestionEvent
    data class OnQueryChange(val query: String): SearchSuggestionEvent
    data class OnSuggestionClick(val id: Int): SearchSuggestionEvent
}