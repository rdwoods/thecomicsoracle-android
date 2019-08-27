package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json

data class ComicCharacterResponse (

    @Json(name = "results")
    val comicCharacters: List<ComicCharacter>?
)