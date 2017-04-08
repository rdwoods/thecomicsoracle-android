package com.rwoods.thecomicsoracle.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class ComicCharacterResponse {


    @JsonField(name = "results")
    List<ComicCharacter> comicCharacters;

    public ComicCharacterResponse(List<ComicCharacter> comicCharacters) {
        this.comicCharacters = comicCharacters;
    }

    public ComicCharacterResponse() {
    }

    public List<ComicCharacter> getComicCharacters() {
        return comicCharacters;
    }

    public void setComicCharacters(List<ComicCharacter> comicCharacters) {
        this.comicCharacters = comicCharacters;
    }
}