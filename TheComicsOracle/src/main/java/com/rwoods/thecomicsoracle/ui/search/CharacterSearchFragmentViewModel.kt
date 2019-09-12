package com.rwoods.thecomicsoracle.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.remote.response.ComicCharacterResponse
import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.ui.base.ComicsOracleBaseViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterSearchFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application) {

    internal var searchedComicCharacterList: ArrayList<ComicCharacter>? = null

    internal var searchedComicsCharacters: MutableLiveData<ArrayList<ComicCharacter>>? = null

    var progressBarLiveData: MutableLiveData<Boolean> = MutableLiveData()

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<ComicCharacter>>? = null

    internal var comicCharacters =  MutableLiveData<ArrayList<ComicCharacter>>()

    internal var savedCharacters: ArrayList<ComicCharacter>? = null

    init {
        comicsOracleRepo = ComicsOracleRepository(application)

        searchedComicCharacterList = ArrayList()

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = moshi!!.adapter<List<ComicCharacter>>(type)
    }


    fun getCharactersFromRest(searchText: String) {

        progressBarLiveData.postValue(true)

        val filteredCharacterName = "name:$searchText"
        comicsOracleRepo.retrofitWrapper.createComicsOracleService().getCharacterByName(filteredCharacterName).enqueue(object : Callback<ComicCharacterResponse> {

            override fun onResponse(call: Call<ComicCharacterResponse>, response: Response<ComicCharacterResponse>) {
                val characters = response.body()?.comicCharacters as? ArrayList<ComicCharacter>

                progressBarLiveData.postValue(false)

                characters?.apply {
                    savedCharacters = characters
                    comicCharacters.postValue(this)
                } ?: run {
                    comicCharacters.postValue(null)
                }
            }

            override fun onFailure(call: Call<ComicCharacterResponse>, t: Throwable) {
                progressBarLiveData.postValue(false)
                comicCharacters.postValue(null)
            }

        })
    }

    fun getSavedData(): String? {
        return jsonAdapter!!.toJson(savedCharacters)
    }


    fun setSavedSearchResults(savedSearchTerm: String, savedData: String) {
        comicsOracleRepo.sharedPreferencesHelper.setSavedSearchResults(savedSearchTerm, savedData)
    }

    fun clearSearchResultsPreferences() {
        comicsOracleRepo.sharedPreferencesHelper.clearSearchResultsPreferences()
    }

    fun getSavedSearchTerm(): String? {
        return comicsOracleRepo.sharedPreferencesHelper.savedSearchTerm
    }
}
