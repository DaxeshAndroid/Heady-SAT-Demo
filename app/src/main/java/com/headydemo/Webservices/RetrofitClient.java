package com.headydemo.Webservices;

import com.headydemo.MyApplication;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builderOkHttp = new OkHttpClient.Builder();
            builderOkHttp.connectTimeout(60, TimeUnit.SECONDS);
            builderOkHttp.readTimeout(60, TimeUnit.SECONDS);
            OkHttpClient client = builderOkHttp.build();
            client = builderOkHttp.addInterceptor(logInterceptor).build();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(MyApplication.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}