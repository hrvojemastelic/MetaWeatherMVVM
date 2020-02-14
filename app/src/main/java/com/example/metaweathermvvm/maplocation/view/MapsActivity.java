package com.example.metaweathermvvm.maplocation.view;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.metaweathermvvm.R;
import com.example.metaweathermvvm.adapter.ForecastAdapter;
import com.example.metaweathermvvm.viewmodel.ForecastViewModel;
import com.example.metaweathermvvm.pojo.ConsolidatedWeather;
import com.example.metaweathermvvm.pojo.LocationModel;
import com.example.metaweathermvvm.pojo.ResponseModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lattlong;
    Button getforecast;
    TextView textCityName;
    List<ConsolidatedWeather> consolidatedWeatherList =new ArrayList<>();
    ForecastAdapter forecastAdapter;
    RecyclerView recyclerView;
    double latitude,longitude;
    ForecastViewModel forecastViewModel;
    MapViewModel mapViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getforecast=findViewById(R.id.button);
        textCityName=findViewById(R.id.maps_cityname);
        recyclerView=findViewById(R.id.rec_maps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        forecastAdapter=new ForecastAdapter(this,consolidatedWeatherList);
        recyclerView.setAdapter(forecastAdapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        forecastViewModel=ViewModelProviders.of(this).get(ForecastViewModel.class);
        getforecast.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forecastViewModel.setLattLong(latitude,longitude);
                forecastViewModel.getLocationLiveData().observe(MapsActivity.this, new Observer<List<LocationModel>>() {
                    @Override
                    public void onChanged(List<LocationModel> locationModels) {
                        LocationModel locationModel = locationModels.get(0);
                        textCityName.setText(locationModel.getTitle());


                    }

                });
                forecastViewModel.getForecastResponseLiveData().observe(MapsActivity.this, new Observer<ResponseModel>() {
                    @Override
                    public void onChanged(ResponseModel responseModel) {
                        consolidatedWeatherList.clear();
                        List<ConsolidatedWeather> consolidatedWeathers=responseModel.getConsolidatedWeather();
                        consolidatedWeatherList.addAll(consolidatedWeathers);
                        forecastAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude+":"+latLng.longitude);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
                googleMap.addMarker(markerOptions);
                latitude=latLng.latitude;
                longitude=latLng.longitude;

            }
        });
    }
}
