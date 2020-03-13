package com.rwoods.thecomicsoracle.ui.characters

import com.rwoods.thecomicsoracle.data.model.ComicCharacter

sealed class CharacterSearchState {
    object LoadingState : CharacterSearchState()
    data class DataState(val data: MutableList<ComicCharacter>) : CharacterSearchState()
    data class ErrorState(val data: String) : CharacterSearchState()
    object FinishState : CharacterSearchState()

}