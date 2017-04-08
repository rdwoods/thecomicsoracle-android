package com.rwoods.thecomicsoracle.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class VideoResponse {


    @JsonField(name = "results")
    List<Video> videos;

    public VideoResponse(List<Video> videos) {
        this.videos = videos;
    }

    public VideoResponse() {
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setCharacters(List<Video> videos) {
        this.videos = videos;
    }
}