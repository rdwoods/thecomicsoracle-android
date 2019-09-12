package com.rwoods.thecomicsoracle.data.model

import com.squareup.moshi.Json

data class ComicCharacter(

    var name: String? = null,

    var gender: String? = null,

    @Json(name = "image")
    var image: Image? = null,

    var description: String? = null
)