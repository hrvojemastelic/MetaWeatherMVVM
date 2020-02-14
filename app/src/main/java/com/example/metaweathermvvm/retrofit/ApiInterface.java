package com.example.metaweathermvvm.retrofit;

import com.example.metaweathermvvm.pojo.LocationModel;
import com.example.metaweathermvvm.pojo.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/api/location/search/")
    Call<List<LocationModel>> getName(@Query("query") String query);

    @GET("/api/location/{woeid}")
    Call<ResponseModel> getFiveDayForecast(@Path("woeid") int woeid);

    @GET("/api/location/search/")
    Call<List<LocationModel>> getLocation(@Query("lattlong") String lattlong);
}
