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
            SearchSuggestionEvent.OnClearQuery -> clearQuery()
            else -> Unit
        }
    }

    private fun clearQuery() {
        _viewState.update { SearchSuggestionViewState() }
    }

    private fun updateQueryAndSearch(event: SearchSuggestionEvent.OnQueryChange) {
        val isQueryBlank = event.query.isBlank()
        _viewState.update {
            it.copy(
                query = event.query,
                isInitialState = if (isQueryBlank) true else it.isInitialState,
                isEmptyState = if (isQueryBlank) false else it.isEmptyState,
                isLoading = false,
                showDeleteIcon = !isQueryBlank,
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
                    isEmptyState = false,
                    isInitialState = false,
                    isLoading = true
                )
            }

            val suggestions = searchRepository.getSearchSuggestions(query)

            _viewState.update {
                it.copy(
                    isEmptyState = suggestions.isEmpty(),
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
                    isEmptyState = true
                )
            }
        }
    }
}