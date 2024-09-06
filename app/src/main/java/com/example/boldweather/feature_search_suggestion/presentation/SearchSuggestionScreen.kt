package com.example.boldweather.feature_search_suggestion.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.boldweather.common.presentation.ForecastDetailScreen
import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO
import kotlinx.serialization.json.JsonNull.content

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SearchSuggestionRoot(
    navigator: ThreePaneScaffoldNavigator<Any>,
    viewModel: SearchSuggestionViewModel
) {
    val state by viewModel.viewState.collectAsState()
    SearchSuggestionScreen(
        onEvent = { event ->
            when (event) {
                is SearchSuggestionEvent.OnSuggestionClick -> {
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        content = event.id
                    )
                }

                else -> Unit
            }
            viewModel.onEvent(event)
        },
        state = state
    )
}

@Composable
fun SearchSuggestionScreen(
    onEvent: (SearchSuggestionEvent) -> Unit,
    state: SearchSuggestionViewState
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = state.query,
            onValueChange = {
                onEvent(SearchSuggestionEvent.OnQueryChange(it))
            },
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(focusedTextColor = Color.Black)
        )
        when {
            state.showSearchHint -> InitialStateComposable()
            state.isLoading -> LoadingStateComposable()
            state.showEmptyState -> EmptyStateComposable()
            else -> SuccessfulStateComposable(
                onClick = { onEvent(SearchSuggestionEvent.OnSuggestionClick(it)) },
                suggestions = state.suggestions
            )
        }
    }
}

@Composable
private fun ColumnScope.SuccessfulStateComposable(
    onClick: (Int) -> Unit,
    suggestions: List<SearchSuggestionDTO>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(suggestions) { suggestion ->
            Text(
                text = "${suggestion.name}, ${suggestion.region}, ${suggestion.country}",
                color = Color.Black,
                modifier = Modifier.clickable { onClick(suggestion.id) }
            )
        }
    }
}

@Composable
private fun InitialStateComposable(modifier: Modifier = Modifier) {

}

@Composable
private fun EmptyStateComposable(modifier: Modifier = Modifier) {

}

@Composable
private fun LoadingStateComposable(modifier: Modifier = Modifier) {

}