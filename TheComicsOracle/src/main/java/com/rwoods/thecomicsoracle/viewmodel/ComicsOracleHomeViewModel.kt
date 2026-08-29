package com.rwoods.thecomicsoracle.viewmodel

import com.rwoods.thecomicsoracle.ui.HomeEffect
import com.rwoods.thecomicsoracle.ui.HomeIntent
import com.rwoods.thecomicsoracle.ui.HomeState
import com.rwoods.thecomicsoracle.viewmodel.base.BaseViewModel
import com.rwoods.thecomicsoracle.usecase.ComicsOracleHomeCharacterUseCase
import com.rwoods.thecomicsoracle.usecase.ComicsOracleVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ComicsOracleHomeViewModel @Inject constructor(
    private val comicsOracleHomeCharacterUseCase: ComicsOracleHomeCharacterUseCase,
    private val comicsOracleVideoUseCase: ComicsOracleVideoUseCase
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
                            copy(isLoading = false, characters = characters, comicVideos = emptyList(), searchType = HomeState.SearchType.CHARACTER)
                        }
                    }
                }
            }

            is HomeIntent.SearchVideo -> {
                launchCoroutine(getExceptionHandler { e -> handleGetVideoException(e) }){
                    comicsOracleVideoUseCase(intent.searchText).collectLatest { videos ->
                        updateState {
                            copy(isLoading = false, characters = emptyList(), comicVideos = videos, searchType = HomeState.SearchType.VIDEO)
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