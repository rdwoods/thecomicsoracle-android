package com.rwoods.thecomicsoracle.ui.state

import com.rwoods.thecomicsoracle.data.model.ComicCharacter

data class CharacterDetailsState(
    val isLoading: Boolean = true,
    val selected: Boolean = false,
    val error: String? = null
)

sealed interface CharacterDetailsIntent {
    data class SelectOrUnselectFavorite(val selected: Boolean, val character: ComicCharacter) : CharacterDetailsIntent
}

sealed interface CharacterDetailsEffect
