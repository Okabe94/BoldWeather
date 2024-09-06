package com.example.boldweather.feature_search_suggestion.presentation

import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO

data class SearchSuggestionViewState(
    val query: String = "",
    val isInitialState: Boolean = true,
    val isEmptyState: Boolean = false,
    val isLoading: Boolean = false,
    val showDeleteIcon: Boolean = false,
    val suggestions: List<SearchSuggestionDTO> = emptyList()
)
