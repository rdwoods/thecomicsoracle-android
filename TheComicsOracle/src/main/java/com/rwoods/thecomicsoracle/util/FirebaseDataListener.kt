package com.rwoods.thecomicsoracle.util

import com.rwoods.thecomicsoracle.data.model.ComicCharacter

interface FirebaseDataListener {
    fun onDatabaseDataRetrieved(characters: ArrayList<ComicCharacter>)
}