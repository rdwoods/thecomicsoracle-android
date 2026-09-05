package com.rwoods.thecomicsoracle.viewmodel

import com.rwoods.thecomicsoracle.ui.state.CharacterDetailsEffect
import com.rwoods.thecomicsoracle.ui.state.CharacterDetailsIntent
import com.rwoods.thecomicsoracle.ui.state.CharacterDetailsState
import com.rwoods.thecomicsoracle.viewmodel.base.BaseViewModel
import com.rwoods.thecomicsoracle.usecase.ComicsOracleCharacterDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ComicsOracleCharacterDetailViewModel @Inject constructor(
    private val comicsOracleCharacterDetailUseCase: ComicsOracleCharacterDetailUseCase
) :  BaseViewModel<CharacterDetailsIntent, CharacterDetailsState, CharacterDetailsEffect>(initialState = CharacterDetailsState()) {

    override suspend fun reduce(intent: CharacterDetailsIntent) {
        when (intent) {
            is CharacterDetailsIntent.SelectOrUnselectFavorite -> {
                launchCoroutine(getExceptionHandler { e -> handleGetCharacterException(e) }){
                    comicsOracleCharacterDetailUseCase.invoke(intent.selected, intent.character).collectLatest { result ->
                        updateState {
                            copy(isLoading = false, selected = result)
                        }
                    }
                }
            }

            else -> {}
        }
    }

    private fun handleGetCharacterException(e: Throwable) {
        launchExceptionCoroutine {
            updateState { copy(isLoading = false, error = "Errors") }
        }
    }

    private fun handleGetVideoException(e: Throwable) {
        launchExceptionCoroutine {
            updateState { copy(isLoading = false, error = "Errors") }
        }
    }
}