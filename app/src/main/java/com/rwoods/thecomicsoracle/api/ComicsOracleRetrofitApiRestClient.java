package com.rwoods.thecomicsoracle.api;

import com.rwoods.thecomicsoracle.BuildConfig;

import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ComicsOracleRetrofitApiRestClient {

    private static ComicsOracleApiService comicsOracleApiService;

    private static final HashMap<HttpUrl, List<Cookie>> accessibleCookieStore = new HashMap<>();

    private static Retrofit retrofit;

    static {
        setupRestClient();
    }

    private ComicsOracleRetrofitApiRestClient() {
    }

    private static void setupRestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Accept", "application/json")
                                .header("Authorization",
                                        token.getTokenType() + " " + token.getAccessToken())
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });*/
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ENV)
                //.addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();

        comicsOracleApiService = retrofit.create(ComicsOracleApiService.class);
    }

    public static ComicsOracleApiService getApiClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        return comicsOracleApiService;
    }

    public static HashMap<HttpUrl, List<Cookie>> getAccessibleCookieStore() {
        return accessibleCookieStore;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}