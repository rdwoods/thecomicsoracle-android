package com.rwoods.thecomicsoracle.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import com.rwoods.thecomicsoracle.R

/**
 * Created by rahmandunbarwoods on 7/3/17.
 */

class SharedPreferencesHelper(private val application: Application) {

    private val mPreferences: SharedPreferences

    init {
        mPreferences = application.getSharedPreferences(SharedPreferencesHelper.PREFERENCE_FILE, Context.MODE_PRIVATE)
    }


    fun setSavedSearchResults(savedSearchTerm: String, savedData: String) {

        mPreferences.edit()
                .putString(application.getString(R.string.saved_character_search_term), savedSearchTerm)
                .putString(application.getString(R.string.saved_character_results), savedData)
                .apply()
    }

    val savedSearchTerm: String
        get() = mPreferences.getString(application.getString(R.string.saved_character_search_term), "")


    fun clearSearchResultsPreferences() {
        mPreferences.edit()
                .remove(application.getString(R.string.saved_character_search_term))
                .remove(application.getString(R.string.saved_character_results))
                .apply()
    }

    companion object {

        private val PREFERENCE_FILE = "ComicsOraclePreferences"
    }
}
