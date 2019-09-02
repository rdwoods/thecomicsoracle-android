package com.rwoods.thecomicsoracle.repository

import android.app.Application
import com.rwoods.thecomicsoracle.network.RetrofitWrapper
import com.rwoods.thecomicsoracle.preferences.SharedPreferencesHelper


/**
 * Created by Rahman Woods on 5/30/17.

 */

class ComicsOracleRepository(application: Application) {
    val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(application)
    //private var firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    var retrofitWrapper: RetrofitWrapper = RetrofitWrapper(application)

    /*internal fun writeFavoriteToDatabase(character : ComicCharacter) {
        val ref = firebaseDatabase.child("users").child("rdwoods1")

        ref.setValue(character)
    }*/


    /*internal fun getFavoritesFromFirebase(listener: FirebaseDataListener) {
        val ref = firebaseDatabase.child("users")
        val favoriteCharacters = ArrayList<ComicCharacter>()

        val favoriteCharacterQuery = ref.equalTo("rdwoods1")
        favoriteCharacterQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    val character = singleSnapshot.getValue(ComicCharacter::class.java)
                    character?.apply { favoriteCharacters.add(this) }
                }

                listener.onDatabaseDataRetrieved(favoriteCharacters)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.e(databaseError.toException())
            }
        })
    }*/
}
