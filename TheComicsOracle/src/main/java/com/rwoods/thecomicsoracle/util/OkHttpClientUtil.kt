package com.rwoods.thecomicsoracle.util

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by Rahman Woods
 */
object OkHttpClientUtil {

    private val timeout = 30
    private val timeoutLength = TimeUnit.SECONDS

    fun createClient(cache: Cache, cacheInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(cacheInterceptor)
                .connectTimeout(timeout.toLong(), timeoutLength)
                .readTimeout(timeout.toLong(), timeoutLength)
                .writeTimeout(timeout.toLong(), timeoutLength)
                .build()
    }
}
