package com.rwoods.thecomicsoracle.data.model

import com.squareup.moshi.Json

data class Image (

    @Json(name = "icon_url")
    var iconUrl: String? = null,

    @Json(name = "medium_url")
    var mediumUrl: String? = null,

    @Json(name = "screen_url")
    var screenUrl: String? = null,

    @Json(name = "small_url")
    var smallUrl: String? = null,

    @Json(name = "super_url")
    var superUrl: String? = null,

    @Json(name = "thumb_url")
    var thumbUrl: String? = null,

    @Json(name = "tiny_url")
    var tinyUrl: String? = null
)