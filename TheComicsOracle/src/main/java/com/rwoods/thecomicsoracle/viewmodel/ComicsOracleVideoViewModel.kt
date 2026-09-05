package com.rwoods.thecomicsoracle.viewmodel

import com.rwoods.thecomicsoracle.ui.state.CharacterDetailsEffect
import com.rwoods.thecomicsoracle.ui.state.CharacterDetailsIntent
import com.rwoods.thecomicsoracle.ui.state.CharacterDetailsState
import com.rwoods.thecomicsoracle.ui.state.VideoEffect
import com.rwoods.thecomicsoracle.ui.state.VideoIntent
import com.rwoods.thecomicsoracle.ui.state.VideoState
import com.rwoods.thecomicsoracle.viewmodel.base.BaseViewModel
import com.rwoods.thecomicsoracle.usecase.ComicsOracleCharacterDetailUseCase
import com.rwoods.thecomicsoracle.usecase.ComicsOracleVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ComicsOracleVideoViewModel @Inject constructor(
    private val comicsOracleVideoUseCase: ComicsOracleVideoUseCase
) :  BaseViewModel<VideoIntent, VideoState, VideoEffect>(initialState = VideoState()) {

    override suspend fun reduce(intent: VideoIntent) {
        when (intent) {
            is VideoIntent.LoadVideoUrl -> {
                launchCoroutine(getExceptionHandler { e -> handleGetCharacterException(e) }){
                    updateState {
                        copy(isLoading = false, videoLoaded = true)
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