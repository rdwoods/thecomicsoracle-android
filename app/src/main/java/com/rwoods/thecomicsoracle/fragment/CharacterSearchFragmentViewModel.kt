package com.rwoods.thecomicsoracle.fragment

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.model.ComicCharacterResponse
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterSearchFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application), Callback<ComicCharacterResponse> {

    internal var searchedComicCharacterList: ArrayList<ComicCharacter>? = null

    internal var searchedComicsCharacters: MutableLiveData<ArrayList<ComicCharacter>> ? = null

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<ComicCharacter>>? = null

    internal var comicCharacters: MutableLiveData<ArrayList<ComicCharacter>>? = null

    internal var savedCharacters: ArrayList<ComicCharacter>? = null


    fun setUp() {
        comicsOracleRepo = ComicsOracleRepository(application)

        searchedComicCharacterList = ArrayList()

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = moshi!!.adapter<List<ComicCharacter>>(type)
    }


    fun getCharactersFromRest(searchText: String) {

        val filteredCharacterName = "name:$searchText"
        val characterSearchCall = ComicsOracleRetrofitApiRestClient.apiClient?.getCharacterByName(filteredCharacterName)

        characterSearchCall?.enqueue(this)
    }

    fun getSavedData(): String? {
        return jsonAdapter!!.toJson(savedCharacters)
    }

    override fun onResponse(call: Call<ComicCharacterResponse>?, response: Response<ComicCharacterResponse>?) {
        val comicCharacters = response?.body()?.comicCharacters as? ArrayList<ComicCharacter>

        if (comicCharacters == null) {
            this.comicCharacters?.postValue(null)
            //mView?.displayCharacterSearchNotRetrieved()
        } else {
            //mView?.displayCharactersFromRest(comicCharacters)
            savedCharacters = comicCharacters;
            this.comicCharacters?.postValue(comicCharacters)
        }
    }

    override fun onFailure(call: Call<ComicCharacterResponse>?, t: Throwable?) {
        comicCharacters?.postValue(null)
    }

    fun setSavedSearchResults(savedSearchTerm: String, savedData: String) {
        comicsOracleRepo?.helper?.setSavedSearchResults(savedSearchTerm, savedData)
    }

    fun clearSearchResultsPreferences() {
        comicsOracleRepo?.helper?.clearSearchResultsPreferences()
    }

    fun getSavedSearchTerm(): String? {
        return comicsOracleRepo?.helper?.savedSearchTerm
    }
}
