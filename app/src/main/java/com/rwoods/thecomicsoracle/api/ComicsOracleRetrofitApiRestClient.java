package com.rwoods.thecomicsoracle.api;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.rwoods.thecomicsoracle.util.Constants;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import java.util.HashMap;
import java.util.List;

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
        // Creates the json object which will manage the information received
//        GsonBuilder builder = new GsonBuilder();
//
//        // Register an com.rwoods.thecomicsoracle.adapter to manage the date types as long values
//        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//            public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                return new Timestamp(json.getAsJsonPrimitive().getAsLong());
//            }
//        });
//
//        Gson gson = builder.create();

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
                .baseUrl(Constants.CV_BASE_URL)
                //.addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(LoganSquareConverterFactory.create())
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