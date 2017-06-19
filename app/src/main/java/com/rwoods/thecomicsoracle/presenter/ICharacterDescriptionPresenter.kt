package com.rwoods.thecomicsoracle.presenter

import android.widget.Button

import com.rwoods.thecomicsoracle.model.ComicCharacter

/**
 * Created by rahmanwoods on 4/8/17.
 */
interface ICharacterDescriptionPresenter {
    fun addOrRemoveToFavorites(comicCharacter: ComicCharacter, isFavorite: Boolean, button: Button)
}
