package com.rwoods.thecomicsoracle.api;

import com.rwoods.thecomicsoracle.entity.ComicCharacterResponse;
import com.rwoods.thecomicsoracle.entity.VideoResponse;
import com.rwoods.thecomicsoracle.util.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rwoods on 2/23/2016.
 */
public interface ComicsOracleApiService {

    @GET(Constants.CV_CHARACTERS_SEARCH + Constants.CV_API_KEY + Constants.CV_FORMAT_JSON)
    Call<ComicCharacterResponse> getCharacterByName(@Query(Constants.CV_FILTER_BY_NAME) String characterName);

    @GET(Constants.CV_VIDEO_SEARCH + Constants.CV_API_KEY + Constants.CV_FORMAT_JSON)
    Call<VideoResponse> getVideoByName(@Query(Constants.CV_FILTER_BY_NAME) String videoName);
}