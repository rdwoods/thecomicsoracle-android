package com.rwoods.thecomicsoracle.data.remote.response

import com.rwoods.thecomicsoracle.data.model.Video
import com.squareup.moshi.Json

data class VideoResponse (

    @Json(name = "results")
    val videos: MutableList<Video>?
)