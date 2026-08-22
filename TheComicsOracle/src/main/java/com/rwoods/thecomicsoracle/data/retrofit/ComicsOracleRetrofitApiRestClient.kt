package com.rwoods.thecomicsoracle.data.retrofit

import com.rwoods.thecomicsoracle.BuildConfig
import com.rwoods.thecomicsoracle.data.service.ComicsOracleApiService
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object ComicsOracleRetrofitApiRestClient {

    // Return the synchronous HTTP client when the thread is not prepared
    var apiClient: ComicsOracleApiService? = null
        private set

    val accessibleCookieStore = HashMap<HttpUrl, List<Cookie>>()

    private val timeout = 30
    private val timeoutLength = TimeUnit.SECONDS


    var retrofit: Retrofit? = null
        private set

    var moshi: Moshi? = null
        private set

    init {
        setupRestClient()
    }

    private fun setupRestClient() {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", "TheComicsOracle/1.0 (Android; Contact: rdwoods1@gmail.com)")
                        .header("Referer", "https://comicvine.gamespot.com/")
                        .header("Accept", "application/json")
                        .build()
                    chain.proceed(request)
                }
                .connectTimeout(timeout.toLong(), timeoutLength)
                .readTimeout(timeout.toLong(), timeoutLength)
                .writeTimeout(timeout.toLong(), timeoutLength)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()).asLenient())
                .client(client)
                .build()

        apiClient = retrofit?.create(ComicsOracleApiService::class.java)
    }
}