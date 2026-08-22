package com.rwoods.thecomicsoracle.ui

import com.rwoods.thecomicsoracle.viewmodel.base.BaseViewModel
import com.rwoods.thecomicsoracle.usecase.ComicsOracleHomeCharacterUseCase
import com.rwoods.thecomicsoracle.usecase.ComicsOracleHomeVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ComicsOracleHomeViewModel @Inject constructor(
    private val comicsOracleHomeCharacterUseCase: ComicsOracleHomeCharacterUseCase,
    private val comicsOracleHomeVideoUseCase: ComicsOracleHomeVideoUseCase
) :  BaseViewModel<HomeIntent, HomeState, HomeEffect>(initialState = HomeState()) {

    override suspend fun reduce(intent: HomeIntent) {
        updateState {
            copy(isLoading = true)
        }

        when (intent) {
            is HomeIntent.SearchCharacter -> {
                launchCoroutine(getExceptionHandler { e -> handleGetCharacterException(e) }){
                    comicsOracleHomeCharacterUseCase(intent.searchText).collectLatest { characters ->
                        updateState {
                            copy(isLoading = false, characters = characters, video = emptyList(), searchType = HomeState.SearchType.CHARACTER)
                        }
                    }
                }
            }

            is HomeIntent.SearchVideo -> {
                launchCoroutine(getExceptionHandler { e -> handleGetVideoException(e) }){
                    comicsOracleHomeVideoUseCase(intent.searchText).collectLatest { videos ->
                        updateState {
                            copy(isLoading = false, characters = emptyList(), video = videos, searchType = HomeState.SearchType.VIDEO)
                        }
                    }
                }
            }
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