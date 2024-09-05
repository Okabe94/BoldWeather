package com.example.boldweather.feature_search_suggestion.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boldweather.di.AppDispatcher
import com.example.boldweather.feature_search_suggestion.domain.SearchSuggestionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SEARCH_DELAY_MILLIS = 500L

class SearchSuggestionViewModel(
    private val searchRepository: SearchSuggestionRepository,
    private val dispatchers: AppDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow(SearchSuggestionViewState())
    val viewState = _viewState.asStateFlow()

    private var searchDelayJob: Job? = null

    fun onEvent(event: SearchSuggestionEvent) {
        when (event) {
            is SearchSuggestionEvent.OnQueryChange -> updateQueryAndSearch(event)
            else -> Unit
        }
    }

    private fun updateQueryAndSearch(event: SearchSuggestionEvent.OnQueryChange) {
        val isQueryBlank = event.query.isBlank()
        _viewState.update {
            it.copy(
                query = event.query,
                showSearchHint = if (isQueryBlank) true else it.showSearchHint,
                showEmptyState = if (isQueryBlank) false else it.showEmptyState,
                isLoading = false,
                suggestions = if (isQueryBlank) emptyList() else it.suggestions
            )
        }
        searchDelayJob?.cancel()

        if (event.query.isNotBlank()) {
            searchDelayJob = searchSuggestions(event.query)
        }
    }

    private fun searchSuggestions(query: String) = viewModelScope.launch(dispatchers.default) {
        try {
            delay(SEARCH_DELAY_MILLIS)
            _viewState.update {
                it.copy(
                    showEmptyState = false,
                    showSearchHint = false,
                    isLoading = true
                )
            }

            val suggestions = searchRepository.getSearchSuggestions(query)

            _viewState.update {
                it.copy(
                    showEmptyState = suggestions.isEmpty(),
                    suggestions = suggestions,
                    isLoading = false
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _viewState.update {
                it.copy(
                    suggestions = emptyList(),
                    isLoading = false,
                    showEmptyState = true
                )
            }
        }
    }
}