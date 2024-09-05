package com.example.boldweather.feature_search_suggestion.data

import com.example.boldweather.feature_search_suggestion.domain.SearchSuggestionMapper
import com.example.boldweather.feature_search_suggestion.domain.model.SearchSuggestionDTO

class SearchSuggestionMapperImpl : SearchSuggestionMapper {

    override fun mapSearchSuggestion(
        response: List<SearchSuggestionApiResponse>
    ): List<SearchSuggestionDTO> = response.map { suggestion ->
        SearchSuggestionDTO(
            name = suggestion.name.orEmpty(),
            region = suggestion.region.orEmpty(),
            country = suggestion.country.orEmpty(),
            lat = suggestion.lat ?: 0.0,
            lon = suggestion.lon ?: 0.0
        )
    }
}