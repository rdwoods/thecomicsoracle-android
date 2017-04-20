package com.rwoods.thecomicsoracle.presenter;

import android.widget.Button;

import com.rwoods.thecomicsoracle.model.ComicCharacter;
import com.rwoods.thecomicsoracle.model.Image;
import com.rwoods.thecomicsoracle.view.CharacterDescriptionActivityView;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rahmanwoods on 4/8/17.
 */
public class CharacterDescriptionPresenter implements ICharacterDescriptionPresenter {
    private Realm mRealm;

    private CharacterDescriptionActivityView view;

    public CharacterDescriptionPresenter(CharacterDescriptionActivityView view) {
        this.view = view;
        
        this.mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void addOrRemoveToFavorites(ComicCharacter comicCharacter, boolean isFavorite, Button btnFavoriteCharacter) {
        ComicCharacter fav;
        if (isFavorite) {
            final RealmResults<ComicCharacter> fcResults = mRealm.where(ComicCharacter.class)
                    .equalTo("id", comicCharacter.getId())
                    .findAll();

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // remove single match
                    fcResults.deleteFirstFromRealm();
                    fcResults.deleteLastFromRealm();
                }
            });


            isFavorite = false;

            btnFavoriteCharacter.setBackgroundResource(android.R.drawable.star_off);
        } else {

            mRealm.beginTransaction();
            fav = mRealm.createObject(ComicCharacter.class); // Create a new object

            fav.setId(comicCharacter.getId());
            fav.setName(comicCharacter.getName());
            fav.setGender(comicCharacter.getGender());
            fav.setDescription(comicCharacter.getDescription());

            if (comicCharacter.getImage() != null) {
                Image image = mRealm.createObject(Image.class);
                image.setIconUrl(comicCharacter.getImage().getIconUrl());
                image.setMediumUrl(comicCharacter.getImage().getMediumUrl());
                image.setScreenUrl(comicCharacter.getImage().getScreenUrl());
                image.setSmallUrl(comicCharacter.getImage().getSmallUrl());
                image.setSuperUrl(comicCharacter.getImage().getSuperUrl());
                image.setThumbUrl(comicCharacter.getImage().getThumbUrl());
                image.setTinyUrl(comicCharacter.getImage().getTinyUrl());
                fav.setImage(image);
            }

            mRealm.commitTransaction();

            isFavorite = true;

            btnFavoriteCharacter.setBackgroundResource(android.R.drawable.star_on);
        }
    }
}
