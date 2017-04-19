package com.rwoods.thecomicsoracle.presenter;

import android.widget.Button;

import com.rwoods.thecomicsoracle.model.ComicCharacter;

/**
 * Created by rahmanwoods on 4/8/17.
 */
public interface ICharacterDescriptionPresenter {
    void addOrRemoveToFavorites(ComicCharacter comicCharacter, boolean isFavorite, Button button);
}
