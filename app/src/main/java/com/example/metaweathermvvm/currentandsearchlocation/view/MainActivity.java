package com.example.metaweathermvvm.currentandsearchlocation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.metaweathermvvm.R;
import com.example.metaweathermvvm.adapter.ForecastAdapter;
import com.example.metaweathermvvm.viewmodel.ForecastViewModel;
import com.example.metaweathermvvm.maplocation.view.MapsActivity;
import com.example.metaweathermvvm.pojo.ConsolidatedWeather;
import com.example.metaweathermvvm.pojo.LocationModel;
import com.example.metaweathermvvm.pojo.ResponseModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements LifecycleOwner  {
    public static final int REQUEST_CODE_LOCATION_PERMISSION=1;
    private RecyclerView recyclerView;
    public List<ConsolidatedWeather> consolidatedWeatherslist=new ArrayList<>();
    ForecastAdapter forecastAdapter;
    private androidx.appcompat.widget.SearchView searchView;
    private TextView textCityName, textWoeid,textLocationType;
    FloatingActionButton floatingActionButton;
    ForecastViewModel forecastViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_search_layout);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        textCityName=findViewById(R.id.cityname);
        textLocationType=findViewById(R.id.location_type);
        textWoeid=findViewById(R.id.woeid);
        searchView=findViewById(R.id.search_view);
        recyclerView=findViewById(R.id.recycler_vieww);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        forecastAdapter=new ForecastAdapter(this, consolidatedWeatherslist);
        recyclerView.setAdapter(forecastAdapter);

        //ASK FOR LOCATION PERMISSION
        //PERMISSION GRANTED CHECK FOR CURRENT LOCATION
        forecastViewModel=ViewModelProviders.of(this).get(ForecastViewModel.class);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
            getForecast();

        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                forecastViewModel.setQuery(query);
                forecastViewModel.getCitynameLiveData().observe(MainActivity.this, new Observer<List<LocationModel>>() {
                    @Override
                    public void onChanged(List<LocationModel> locationModels) {
                        LocationModel locationModel = locationModels.get(0);
                        textCityName.setText(locationModel.getTitle());
                        textLocationType.setText(locationModel.getLocationType());
                        textWoeid.setText(String.valueOf(locationModel.getWoeid()));

                    }
                });
                getForecast();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }



    //CHECK FOR PERMISSION RESULTS IF PERMISSION IS GRANTED GET CURRENT LOCATION
    // IF NOT PRINT TOAST MESSAGE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if (grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
                getForecast();
            }
            else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getForecast() {
        forecastViewModel.getForecastResponseLiveData().observe(MainActivity.this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                consolidatedWeatherslist.clear();
                List<ConsolidatedWeather> consolidatedWeathers=responseModel.getConsolidatedWeather();
                consolidatedWeatherslist.addAll(consolidatedWeathers);
                forecastAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(MainActivity.this).
                requestLocationUpdates(locationRequest,new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult!=null && locationResult.getLocations().size()>0){
                            int latestlocationindex=locationResult.getLocations().size()-1;
                         double   latitude=locationResult.getLocations().get(latestlocationindex).getLatitude();
                          double  longitude=locationResult.getLocations().get(latestlocationindex).getLongitude();
                           forecastViewModel.setLattLong(latitude,longitude);
                          forecastViewModel.getLocationLiveData().observe(MainActivity.this, new Observer<List<LocationModel>>() {
                              @Override
                              public void onChanged(List<LocationModel> locationModels) {
                                  LocationModel locationModel = locationModels.get(0);
                                  textCityName.setText(locationModel.getTitle());
                                  textLocationType.setText(locationModel.getLocationType());
                                  textWoeid.setText(String.valueOf(locationModel.getWoeid()));

                              }
                          });
                            //PASS LATITUDE AND LONGITUDE TO INTENT AN

                        }

                    }
                }, Looper.getMainLooper());
    }

}
