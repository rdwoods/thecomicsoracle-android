package com.rwoods.thecomicsoracle.ui.favorite

import android.app.Application
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.ui.base.ComicsOracleBaseViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class FavoriteCharacterFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application) {
    private var fragmentName: String? = null

    //internal var mView: FavoriteCharactersFragment? = null

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<ComicCharacter>>? = null

    fun setUp() {
        comicsOracleRepo = ComicsOracleRepository(application)

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = moshi?.adapter<List<ComicCharacter>>(type)
    }
}
