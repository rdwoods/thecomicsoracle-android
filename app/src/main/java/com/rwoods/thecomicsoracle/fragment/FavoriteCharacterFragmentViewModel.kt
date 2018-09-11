package com.rwoods.thecomicsoracle.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class FavoriteCharacterFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application) {
    private var fragmentName: String? = null

    internal var mView: FavoriteCharactersFragment? = null

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<ComicCharacter>>? = null

    fun setUp() {
        comicsOracleRepo = ComicsOracleRepository(application)

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = moshi!!.adapter<List<ComicCharacter>>(type)
    }
}
