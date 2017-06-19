package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json

data class VideoResponse (

    @Json(name = "results")
    val videos: List<Video>?
)