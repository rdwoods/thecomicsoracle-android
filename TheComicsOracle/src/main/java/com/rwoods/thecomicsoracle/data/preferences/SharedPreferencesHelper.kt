package com.rwoods.thecomicsoracle.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.rwoods.thecomicsoracle.R

class SharedPreferencesHelper(private val context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)

    fun setSavedSearchResults(savedSearchTerm: String, savedData: String) {
        preferences.edit {
            putString(context.getString(R.string.saved_character_search_term), savedSearchTerm)
            putString(context.getString(R.string.saved_character_results), savedData)
        }
    }

    val savedSearchTerm: String
        get() = preferences.getString(context.getString(R.string.saved_character_search_term), "") ?: ""

    fun clearSearchResultsPreferences() {
        preferences.edit {
            remove(context.getString(R.string.saved_character_search_term))
            remove(context.getString(R.string.saved_character_results))
        }
    }

    companion object {
        private const val PREFERENCE_FILE = "ComicsOraclePreferences"
    }
}
