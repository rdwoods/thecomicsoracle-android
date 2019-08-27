package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json

data class Video (

    var id: Long = 0,

    var name: String? = null,

    @Json(name = "image")
    var image: Image? = null,

    @Json(name = "high_url")
    var highUrl: String? = null,

    @Json(name = "low_url")
    var lowUrl: String? = null
)