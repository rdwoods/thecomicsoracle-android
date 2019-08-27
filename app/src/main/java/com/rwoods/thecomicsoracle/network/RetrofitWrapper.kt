package com.rwoods.thecomicsoracle.network

import android.content.Context
import com.rwoods.thecomicsoracle.api.ComicsOracleApiService
import com.rwoods.thecomicsoracle.util.RetrofitUtil
import retrofit2.Retrofit

/**
 * Created by Rahman Woods on 5/25/18.
 */
class RetrofitWrapper(ctx: Context) {
    private val retrofit: Retrofit = RetrofitUtil.buildRetrofitClient(ctx)

    fun createComicsOracleService(): ComicsOracleApiService {
        return retrofit.create(ComicsOracleApiService::class.java)
    }
}
