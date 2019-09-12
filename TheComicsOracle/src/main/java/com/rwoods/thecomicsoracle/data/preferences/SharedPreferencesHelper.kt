package com.rwoods.thecomicsoracle.data.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import com.rwoods.thecomicsoracle.R

/**
 * Created by rahmandunbarwoods on 7/3/17.
 */

class SharedPreferencesHelper(private val application: Application) {

    private val preferences: SharedPreferences

    init {
        preferences = application.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
    }


    fun setSavedSearchResults(savedSearchTerm: String, savedData: String) {

        preferences.edit()
                .putString(application.getString(R.string.saved_character_search_term), savedSearchTerm)
                .putString(application.getString(R.string.saved_character_results), savedData)
                .apply()
    }

    val savedSearchTerm: String
        get() = preferences.getString(application.getString(R.string.saved_character_search_term), "")
                ?: run { "" }


    fun clearSearchResultsPreferences() {
        preferences.edit()
                .remove(application.getString(R.string.saved_character_search_term))
                .remove(application.getString(R.string.saved_character_results))
                .apply()
    }

    companion object {

        private val PREFERENCE_FILE = "ComicsOraclePreferences"
    }
}
