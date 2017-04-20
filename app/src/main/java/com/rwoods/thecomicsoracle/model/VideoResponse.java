package com.rwoods.thecomicsoracle.model;

import com.squareup.moshi.Json;

import java.util.List;

public class VideoResponse {


    @Json(name = "results")
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