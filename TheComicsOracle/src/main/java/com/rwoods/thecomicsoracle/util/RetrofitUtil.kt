package com.rwoods.thecomicsoracle.util

import android.content.Context
import androidx.annotation.NonNull
import com.rwoods.thecomicsoracle.BuildConfig
import com.rwoods.thecomicsoracle.data.interceptor.CacheInterceptor
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Cache
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

object RetrofitUtil {

    private val baseUrl: String
        get() = BuildConfig.BASE_URL

    fun formatAccessToken(@NonNull accessToken: String): String {
        return String.format("Bearer %s", accessToken)
    }


    fun buildRetrofitClient(@NonNull ctx: Context): Retrofit {

        val cacheSize = (10 * 1024 * 1024).toLong()
        val httpCacheDirectory = File(ctx.cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize)


        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpClientUtil.createClient(cache, CacheInterceptor()))
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .build()
    }
}
