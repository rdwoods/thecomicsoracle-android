package com.rwoods.thecomicsoracle.repository

import android.app.Application
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.preferences.SharedPreferencesHelper
import io.realm.Realm
import io.realm.RealmResults


/**
 * Created by Rahman Woods on 5/30/17.

 */

class ComicsOracleRepository(application: Application) {
    val helper: SharedPreferencesHelper = SharedPreferencesHelper(application)
    internal var realm: Realm? = null

    init {
        realm = Realm.getDefaultInstance()
    }

    fun onResume() {
        realm = Realm.getDefaultInstance()
    }

    fun onDestroy() {
        if (realm != null) {
            realm!!.close()
        }
    }

    internal fun getFavoritesFromDatabase(): RealmResults<ComicCharacter>? {
        val fcResults = realm!!.where(ComicCharacter::class.java).findAll()

        return fcResults
    }
}
