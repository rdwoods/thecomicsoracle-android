package com.rwoods.thecomicsoracle.data.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val cacheControl = CacheControl.Builder()
            .maxAge(7, TimeUnit.DAYS)
            .maxStale(8, TimeUnit.DAYS)
            .build()

        val request = chain.request().newBuilder()
            .cacheControl(cacheControl)
            .build()

        return chain.proceed(request)
    }
}
