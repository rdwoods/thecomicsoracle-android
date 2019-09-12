package com.rwoods.thecomicsoracle.data.service

import com.rwoods.thecomicsoracle.BuildConfig
import com.rwoods.thecomicsoracle.data.remote.response.ComicCharacterResponse
import com.rwoods.thecomicsoracle.data.remote.response.VideoResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by rwoods on 2/23/2016.
 */
interface ComicsOracleApiService {

    @GET("characters/?api_key=" + BuildConfig.API_KEY + "&format=json")
    fun getCharacterByName(@Query("filter") characterName: String): Call<ComicCharacterResponse>

    @GET("videos/?api_key=" + BuildConfig.API_KEY + "&format=json")
    fun getVideoByName(@Query("filter") videoName: String): Call<VideoResponse>
}