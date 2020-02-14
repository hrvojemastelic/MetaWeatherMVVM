package com.example.metaweathermvvm.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL="https://www.metaweather.com/api/";
    private static Retrofit retrofit;

    public static   Retrofit getRetrofitInstance(){

        retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;

    }
}
