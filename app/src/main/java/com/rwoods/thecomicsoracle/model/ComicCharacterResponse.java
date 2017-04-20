package com.rwoods.thecomicsoracle.model;

import com.squareup.moshi.Json;

import java.util.List;

public class ComicCharacterResponse {


    @Json(name = "results")
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