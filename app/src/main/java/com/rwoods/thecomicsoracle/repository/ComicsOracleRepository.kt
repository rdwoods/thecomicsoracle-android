package com.rwoods.thecomicsoracle.repository

import android.app.Application
import android.content.Context
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.preferences.SharedPreferencesHelper
import io.realm.Realm
import io.realm.RealmResults


/**
 * Created by Alex Pritchard on 5/30/17.

 */

class ComicsOracleRepository(application: Application) {
    val helper: SharedPreferencesHelper = SharedPreferencesHelper(application)
    internal var mRealm: Realm? = null

    init {
        mRealm = Realm.getDefaultInstance()
    }

    fun onResume() {
        mRealm = Realm.getDefaultInstance()
    }

    fun onDestroy() {
        if (mRealm != null) {
            mRealm!!.close()
        }
    }

    internal fun getFavoritesFromDb(): RealmResults<ComicCharacter>? {
        val fcResults = mRealm!!.where(ComicCharacter::class.java).findAll()

        return fcResults
    }
}
