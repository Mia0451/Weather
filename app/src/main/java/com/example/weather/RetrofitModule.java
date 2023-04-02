package com.example.weather;

import android.util.Log;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module installed in singleton component, provided Retrofit configuration.
 */
@InstallIn(SingletonComponent.class)
@Module
public class RetrofitModule {
    // Use https, ending with forward slash.
    private static final String BASE_URL = "https://api.openweathermap.org/";
    // TODO(wenyi): should be secured somewhere else.
    private static final String API_KEY = "a420ec9b6936d9212ca1d70b60eacf3d";

    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                // Append APPID query param to every outgoing http request.
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("APPID", API_KEY)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                Log.e("here", "here in RetrofitModule, url = " + request.url());
                Response response = chain.proceed(request);
                return  response;
            }
        });
        return httpClientBuilder.build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
}
