package com.rwoods.thecomicsoracle.api

import com.rwoods.thecomicsoracle.BuildConfig
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi

import java.io.IOException
import java.util.HashMap

import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ComicsOracleRetrofitApiRestClient {

    // Return the synchronous HTTP client when the thread is not prepared
    var apiClient: ComicsOracleApiService? = null
        private set

    val accessibleCookieStore = HashMap<HttpUrl, List<Cookie>>()

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
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest;
                        int maxAge = 60 * 60;
                        newRequest = request.newBuilder()
                                .addHeader("Accept", "application/json")
                                .addHeader("Cache-Control", "public, max-age=" + maxAge)
                                .build();


                        return chain.proceed(newRequest);
                    }
                })*/
                .build()

        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.ENV)
                //.addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .client(client)
                .build()

        apiClient = retrofit!!.create(ComicsOracleApiService::class.java)
    }
}