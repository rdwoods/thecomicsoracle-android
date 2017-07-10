package com.rwoods.thecomicsoracle.preferences

import android.content.Context
import android.content.SharedPreferences

import com.rwoods.thecomicsoracle.R

/**
 * Created by rahmandunbarwoods on 7/3/17.
 */

class SharedPreferencesHelper(private val mContext: Context) {

    private val mPreferences: SharedPreferences

    init {
        mPreferences = mContext.getSharedPreferences(SharedPreferencesHelper.PREFERENCE_FILE, Context.MODE_PRIVATE)
    }


    fun setSavedSearchResults(savedSearchTerm: String, savedData: String) {

        mPreferences.edit()
                .putString(mContext.getString(R.string.saved_character_search_term), savedSearchTerm)
                .putString(mContext.getString(R.string.saved_character_results), savedData)
                .apply()
    }

    val savedSearchResults: String
        get() = mPreferences.getString(mContext.getString(R.string.saved_character_search_term), "")


    fun clearSearchResultsPreferences() {
        mPreferences.edit()
                .remove(mContext.getString(R.string.saved_character_search_term))
                .remove(mContext.getString(R.string.saved_character_results))
                .apply()
    }

    companion object {

        private val PREFERENCE_FILE = "ComicsOraclePreferences"
    }
}
