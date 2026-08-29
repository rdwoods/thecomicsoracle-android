package com.rwoods.thecomicsoracle.ui.state

import com.rwoods.thecomicsoracle.data.model.ComicCharacter

data class VideoState(
    val isLoading: Boolean = true,
    val videoLoaded: Boolean = false,
    val highResVideoUrl: String? = null,
    val lowResVideoUrl: String? = null,
    val error: String? = null
)

sealed interface VideoIntent {
    object LoadVideoUrl : VideoIntent

    object PlayLowResVideo : VideoIntent
}

sealed interface VideoEffect
