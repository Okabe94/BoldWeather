package com.example.boldweather.feature_search_suggestion.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boldweather.R
import com.example.boldweather.common.presentation.DefaultCardComposable
import com.example.boldweather.common.presentation.LoadingStateComposable
import com.example.boldweather.common.presentation.ScreenBackground
import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO
import com.example.boldweather.ui.theme.BoldWeatherTheme

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
                        pane = ListDetailPaneScaffoldRole.Detail, content = event.id
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
    ScreenBackground {
        ScreenTitle()
        SearchTextField(
            state = state,
            onValueChange = { onEvent(SearchSuggestionEvent.OnQueryChange(it)) },
            onClearClick = { onEvent(SearchSuggestionEvent.OnClearQuery) }
        )
        when {
            state.isInitialState -> InitialStateComposable()
            state.isLoading -> LoadingStateComposable()
            state.isEmptyState -> EmptyStateComposable()
            else -> SuccessfulStateComposable(
                onClick = { onEvent(SearchSuggestionEvent.OnSuggestionClick(it)) },
                suggestions = state.suggestions
            )
        }
    }
}

@Composable
private fun ColumnScope.SearchTextField(
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    state: SearchSuggestionViewState
) {
    TextField(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        value = state.query,
        onValueChange = { onValueChange(it) },
        shape = CircleShape,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        trailingIcon = {
            if (state.showDeleteIcon) {
                Icon(
                    modifier = Modifier.clickable { onClearClick() },
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        placeholder = {
            Text(text = stringResource(R.string.search_placeholder_text))
        }
    )
}

@Composable
private fun ScreenTitle() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = stringResource(R.string.search_screen_title),
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ColumnScope.SuccessfulStateComposable(
    onClick: (Int) -> Unit,
    suggestions: List<SearchSuggestionDTO>
) {
    Spacer(modifier = Modifier.height(16.dp))

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(bottom = 12.dp),
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(suggestions) { suggestion ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = MaterialTheme.shapes.large,
                onClick = { onClick(suggestion.id) }
            ) {
                LocationCard(suggestion)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LocationCard(suggestion: SearchSuggestionDTO) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)

        Column {
            val entryLocation = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                ) {
                    append(suggestion.name)
                }
                append(", ")
                append(suggestion.region)
            }
            Text(
                text = suggestion.country,
                fontFamily = FontFamily.Default,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = entryLocation,
                fontFamily = FontFamily.Default,
            )
        }
    }
}

@Composable
private fun EmptyStateComposable() {
    DefaultCardComposable(
        title = stringResource(id = R.string.search_error_title),
        message = stringResource(id = R.string.search_error_message),
        icon = Icons.Outlined.Info
    )
}

@Composable
private fun InitialStateComposable() {
    DefaultCardComposable(
        title = stringResource(R.string.search_empty_state_title),
        message = stringResource(R.string.search_empty_state_message),
        icon = Icons.Default.Place
    )
}

@PreviewLightDark
@Composable
private fun SearchSuggestionScreenPreview() {
    BoldWeatherTheme {
        SearchSuggestionScreen(
            onEvent = {}, state = SearchSuggestionViewState(
                query = "hola mundo",
                isInitialState = false,
                isEmptyState = false,
                isLoading = false,
                suggestions = listOf(
                    SearchSuggestionDTO(
                        id = 2,
                        name = "Medellín",
                        region = "Region",
                        country = "Country",
                        lat = 1.0,
                        lon = 2.9
                    ),
                    SearchSuggestionDTO(
                        id = 2,
                        name = "Medellín",
                        region = "Region",
                        country = "Country",
                        lat = 1.0,
                        lon = 2.9
                    ),
                    SearchSuggestionDTO(
                        id = 2,
                        name = "Medellín",
                        region = "Region",
                        country = "Country",
                        lat = 1.0,
                        lon = 2.9
                    )
                )
            )
        )
    }
}
