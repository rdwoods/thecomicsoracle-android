package com.rwoods.thecomicsoracle.data.service

import com.rwoods.thecomicsoracle.BuildConfig
import com.rwoods.thecomicsoracle.data.remote.response.ComicCharacterResponse
import com.rwoods.thecomicsoracle.data.remote.response.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by rwoods on 2/23/2016.
 */
interface ComicsOracleApiService {

    @GET("characters")
    suspend fun getCharacterByNameAsync(
        @Query("filter") characterName: String,
        @Query("limit") limit: Int = 20
    ): Response<ComicCharacterResponse>

    @GET("videos/")
    suspend fun getVideoByNameAsync(
        @Query("filter") videoName: String,
        @Query("limit") limit: Int = 20
    ): Response<VideoResponse>
}