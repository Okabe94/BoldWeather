package com.example.boldweather.feature_search_suggestion.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO
import com.example.boldweather.ui.theme.BoldWeatherTheme
import com.example.boldweather.ui.theme.DuskNight
import com.example.boldweather.ui.theme.StarrySky
import com.example.boldweather.ui.theme.SunnyYellow
import com.example.boldweather.ui.theme.WarmSunset

private const val ANIMATION_DURATION = 25000
private const val TARGET_OFFSET = 1000
private const val BRUSH_SIZE = 2000f

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

    val bgColors = if (isSystemInDarkTheme()) {
        listOf(DuskNight, StarrySky)
    } else {
        listOf(WarmSunset, SunnyYellow)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val targetOffset = with(LocalDensity.current) { TARGET_OFFSET.dp.toPx() }

    val offset by infiniteTransition.animateFloat(
        label = "offset",
        initialValue = 0f,
        targetValue = targetOffset,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ANIMATION_DURATION,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .drawWithCache {
            val brushSize = BRUSH_SIZE
            val brush = Brush.linearGradient(
                colors = bgColors,
                start = Offset(offset, offset),
                end = Offset(offset + brushSize, offset + brushSize),
            )
            onDrawBehind { drawRect(brush) }
        }) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(R.string.search_screen_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value = state.query,
            onValueChange = {
                onEvent(SearchSuggestionEvent.OnQueryChange(it))
            },
            shape = CircleShape,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (state.showDeleteIcon) {
                    Icon(
                        modifier = Modifier.clickable {
                            onEvent(SearchSuggestionEvent.OnClearQuery)
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            },
            placeholder = {
                Text(text = stringResource(R.string.search_placeholder_text))
            }
        )
        when {
            state.isInitialState -> Unit
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
private fun ColumnScope.SuccessfulStateComposable(
    onClick: (Int) -> Unit,
    suggestions: List<SearchSuggestionDTO>
) {
    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(suggestions) { suggestion ->
            Card(
                shape = MaterialTheme.shapes.large,
                onClick = { onClick(suggestion.id) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {q
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
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = entryLocation,
                            fontFamily = FontFamily.Default,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun EmptyStateComposable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(R.string.search_error_title),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Default,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.search_error_message),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Default,
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.LoadingStateComposable() {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
                Text(text = stringResource(R.string.loading))
            }
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
    }
}

@PreviewLightDark
@Composable
private fun SearchSuggestionScreenPreview() {
    BoldWeatherTheme {
        SearchSuggestionScreen(
            onEvent = {}, state = SearchSuggestionViewState(
                query = "",
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
