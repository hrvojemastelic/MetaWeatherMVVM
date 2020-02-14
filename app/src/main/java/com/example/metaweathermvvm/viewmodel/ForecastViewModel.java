package com.example.metaweathermvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.metaweathermvvm.pojo.LocationModel;
import com.example.metaweathermvvm.pojo.ResponseModel;
import com.example.metaweathermvvm.repository.ForecastRepository;

import java.util.List;

public class ForecastViewModel extends AndroidViewModel {
        private ForecastRepository weatherRepository;
        private LiveData<ResponseModel> forecastResponseLiveData;
        private LiveData<List<LocationModel>> locationLiveData;
        private LiveData<List<LocationModel>> citynameLiveData;
         public ForecastViewModel(@NonNull Application application) {
            super(application);

            weatherRepository=new ForecastRepository();
             this.locationLiveData=weatherRepository.getLocation();
             this.forecastResponseLiveData=weatherRepository.getForecastFivee();
                this.citynameLiveData=weatherRepository.getByCityName();

        }

        public LiveData<List<LocationModel>> getLocationLiveData(){
            return locationLiveData;
        }
        public void setLattLong(double latt, double lng){
            weatherRepository.setLattLong(latt,lng);
        }
        public LiveData<ResponseModel>getForecastResponseLiveData(){
            return forecastResponseLiveData;
        }

        public void setQuery(String query){
             weatherRepository.setQuery(query);
        }
        public LiveData<List<LocationModel>> getCitynameLiveData(){
             return citynameLiveData;
        }


}
