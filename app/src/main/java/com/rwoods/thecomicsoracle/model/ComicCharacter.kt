package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json

open class ComicCharacter {

    var name: String? = null

    var gender: String? = null

    @Json(name = "image")
    var image: Image? = null

    var description: String? = null
}