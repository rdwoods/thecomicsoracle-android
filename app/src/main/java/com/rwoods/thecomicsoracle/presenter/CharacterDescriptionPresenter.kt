package com.rwoods.thecomicsoracle.presenter

import android.widget.Button
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.model.Image
import com.rwoods.thecomicsoracle.view.CharacterDescriptionActivityView
import io.realm.Realm

/**
 * Created by rahmanwoods on 4/8/17.
 */
class CharacterDescriptionPresenter(private val view: CharacterDescriptionActivityView) : ICharacterDescriptionPresenter {
    private val mRealm: Realm

    init {

        this.mRealm = Realm.getDefaultInstance()
    }

    override fun addOrRemoveToFavorites(comicCharacter: ComicCharacter, isFavorite: Boolean, btnFavoriteCharacter: Button) {
        var isFavorite = isFavorite
        val fav: ComicCharacter
        if (isFavorite) {
            val fcResults = mRealm.where(ComicCharacter::class.java)
                    .equalTo("id", comicCharacter.id)
                    .findAll()

            mRealm.executeTransaction {
                // remove single match
                fcResults.deleteFirstFromRealm()
                fcResults.deleteLastFromRealm()
            }


            isFavorite = false

            btnFavoriteCharacter.setBackgroundResource(android.R.drawable.star_off)
        } else {

            mRealm.beginTransaction()
            fav = mRealm.createObject(ComicCharacter::class.java) // Create a new object

            fav.id = comicCharacter.id
            fav.name = comicCharacter.name
            fav.gender = comicCharacter.gender
            fav.description = comicCharacter.description

            if (comicCharacter.image != null) {
                val image = mRealm.createObject(Image::class.java)
                image.iconUrl = comicCharacter.image!!.iconUrl
                image.mediumUrl = comicCharacter.image!!.mediumUrl
                image.screenUrl = comicCharacter.image!!.screenUrl
                image.smallUrl = comicCharacter.image!!.smallUrl
                image.superUrl = comicCharacter.image!!.superUrl
                image.thumbUrl = comicCharacter.image!!.thumbUrl
                image.tinyUrl = comicCharacter.image!!.tinyUrl
                fav.image = image
            }

            mRealm.commitTransaction()

            isFavorite = true

            btnFavoriteCharacter.setBackgroundResource(android.R.drawable.star_on)
        }
    }
}
