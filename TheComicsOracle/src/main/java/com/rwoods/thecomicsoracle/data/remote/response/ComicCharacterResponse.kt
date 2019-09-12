package com.rwoods.thecomicsoracle.data.remote.response

import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.squareup.moshi.Json

data class ComicCharacterResponse (

    @Json(name = "results")
    val comicCharacters: List<ComicCharacter>?
)