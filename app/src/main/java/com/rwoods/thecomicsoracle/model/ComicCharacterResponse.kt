package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json

class ComicCharacterResponse (

    @Json(name = "results")
    val comicCharacters: List<ComicCharacter>?
)