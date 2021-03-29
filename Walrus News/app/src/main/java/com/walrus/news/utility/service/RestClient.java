package com.walrus.news.utility.service;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.walrus.news.utility.AppConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.walrus.news.utility.AppConstants.REQ_AUTHORIZATION;

public class RestClient {
    public static final String BASE_URL = "https://newsapi.org/v2/";
    private static RestApi REST_CLIENT;

    public static RestApi get() {
        return REST_CLIENT;
    }

    public static Gson gson = new GsonBuilder()
//            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES)
            .create();
    OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", REQ_AUTHORIZATION)
                    .build();
            return chain.proceed(request);
        }
    }).build();
    public static Retrofit restAdapter = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getClient())
            .build();


    public static <S> S createService(Class<S> serviceClass) {
        restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
        return restAdapter.create(serviceClass);
    }

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);
        client.connectTimeout(AppConstants.RESPONSE_TIMEOUT, TimeUnit.SECONDS);
        client.readTimeout(AppConstants.RESPONSE_TIMEOUT, TimeUnit.SECONDS);
        return client.build();
    }
}
