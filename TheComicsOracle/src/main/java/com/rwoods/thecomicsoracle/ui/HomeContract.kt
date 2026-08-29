package com.rwoods.thecomicsoracle.ui

import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.model.ComicVideo

data class HomeState(
    val isLoading: Boolean = false,
    val characters: List<ComicCharacter> = emptyList(),
    val comicVideos: List<ComicVideo> = emptyList(),
    val searchType : SearchType = SearchType.NONE,
    val error: String? = null
) {
    enum class SearchType {
        CHARACTER,
        VIDEO,
        NONE
    }
}

sealed interface HomeIntent {
    data class SearchCharacter(val searchText: String) : HomeIntent

    data class SearchVideo(val searchText: String) : HomeIntent
}

sealed interface HomeEffect
