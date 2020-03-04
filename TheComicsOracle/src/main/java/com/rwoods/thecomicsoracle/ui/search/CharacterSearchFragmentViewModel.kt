package com.rwoods.thecomicsoracle.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.ui.base.ComicsOracleBaseViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.*

class CharacterSearchFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application) {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since we pass viewModelJob, you can cancel all coroutines
     * launched by uiScope by calling viewModelJob.cancel()
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var progressBarLiveData: MutableLiveData<Boolean> = MutableLiveData()

    internal var moshi: Moshi

    internal var jsonAdapter: ComicCharacterJsonAdapter

    internal var comicCharactersMutableLiveData =  MutableLiveData<MutableList<ComicCharacter>>()

    internal var savedCharacters = mutableListOf<ComicCharacter>()

    init {
        comicsOracleRepo = ComicsOracleRepository(application)

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = ComicCharacterJsonAdapter(application)
    }


    fun getCharacters(searchText: String) {
        progressBarLiveData.postValue(true)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Dispatchers.IO
                /* perform blocking network IO here */
                val characters = comicsOracleRepo.getCharactersFromRest(searchText)
                progressBarLiveData.postValue(false)

                characters?.run {
                    savedCharacters = this
                    comicCharactersMutableLiveData.postValue(this)
                } ?: run {
                    progressBarLiveData.postValue(false)
                    comicCharactersMutableLiveData.postValue(null)
                }
            }
        }
    }


    fun getSavedData(): String? {
        return jsonAdapter.toString()
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
