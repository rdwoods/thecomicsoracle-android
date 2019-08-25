package com.rwoods.thecomicsoracle.util

import com.rwoods.thecomicsoracle.model.ComicCharacter

interface FirebaseDataListener {
    fun onDatabaseDataRetrieved(characters: ArrayList<ComicCharacter>)
}