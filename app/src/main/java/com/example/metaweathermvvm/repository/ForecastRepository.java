package com.example.metaweathermvvm.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.metaweathermvvm.pojo.LocationModel;
import com.example.metaweathermvvm.pojo.ResponseModel;
import com.example.metaweathermvvm.retrofit.ApiClient;
import com.example.metaweathermvvm.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastRepository {
    private ApiInterface apiRequest;
    private static ForecastRepository INSTANCE;

    final MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
    final MutableLiveData<List<LocationModel>> mutableLiveDataCityName= new MutableLiveData<>();
    final MutableLiveData<List<LocationModel>> locationMutableLiveData = new MutableLiveData<>();

    public static   ForecastRepository getInstance(Context context){
        if ( INSTANCE == null){
            INSTANCE=new ForecastRepository(context.getApplicationContext());
        }
        return INSTANCE;
    }


    public ForecastRepository(Context aplicationContext) {
        apiRequest = ApiClient.getRetrofitInstance().create(ApiInterface.class);

    }

    public void setLattLong(double latt,double Lng) {

        apiRequest.getLocation(String.valueOf(latt+","+Lng)).enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                if (response.body() != null) {
                    LocationModel locationModel  = response.body().get(0);
                   int woeid=locationModel.getWoeid();
                    locationMutableLiveData.setValue(response.body());
                   getWoeid(woeid);
                    System.out.println("grad" + woeid);
                }
            }


            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {

            }
        });
    }


    public void setQuery(String query){
        System.out.println("grad" + query);

        apiRequest.getName(String.valueOf(query)).enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                if (response.isSuccessful()) {
                    LocationModel locationModel  = response.body().get(0);
                  int  woeid=locationModel.getWoeid();
                    mutableLiveDataCityName.setValue(response.body());
                    getWoeid(woeid);
                    System.out.println("grad" + response.toString());

                }
            }


            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {

            }
        });

    }


    public MutableLiveData<List<LocationModel>> getByCityName(){

        return mutableLiveDataCityName;

    }

    public MutableLiveData<List<LocationModel>> getLocation(){
        return locationMutableLiveData;
    }

    public MutableLiveData<ResponseModel>  getForecastFivee(){
        return responseModelMutableLiveData;
    }

    public void getWoeid(int woeid){
        apiRequest.getFiveDayForecast(woeid)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                        if (response.isSuccessful()) {
                            System.out.println("response"+ response.toString());
                            responseModelMutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        responseModelMutableLiveData.setValue(null);
                        System.out.println("Error"+ t.getMessage().toString());
                    }
                });
    }
}
