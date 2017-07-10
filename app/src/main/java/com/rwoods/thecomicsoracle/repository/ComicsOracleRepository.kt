package com.rwoods.thecomicsoracle.repository

import android.content.Context
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.preferences.SharedPreferencesHelper
import io.realm.Realm
import io.realm.RealmResults


/**
 * Created by Alex Pritchard on 5/30/17.

 */

class ComicsOracleRepository(private val mContext: Context) {
    val helper: SharedPreferencesHelper
    internal var mRealm: Realm? = null

    init {
        helper = SharedPreferencesHelper(mContext)
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
