package com.rwoods.thecomicsoracle.data.remote.response

import com.rwoods.thecomicsoracle.data.model.ComicVideo
import com.squareup.moshi.Json

data class VideoResponse (

    @Json(name = "results")
    val comicVideos: MutableList<ComicVideo>?
)