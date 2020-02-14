package com.example.metaweathermvvm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metaweathermvvm.R;
import com.example.metaweathermvvm.pojo.ConsolidatedWeather;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    Context context;
    List<ConsolidatedWeather> consolidatedWeatherArrayList=new ArrayList<>();

    public ForecastAdapter (Context context, List<ConsolidatedWeather> consolidatedWeatherArrayList) {
        this.context = context;
        this.consolidatedWeatherArrayList=consolidatedWeatherArrayList;
    }

    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ViewHolder holder, int position) {
        ConsolidatedWeather  postModel=consolidatedWeatherArrayList.get(position);

        holder.windSpeed.setText(String.valueOf("Wind Speed : "+postModel.getWindSpeed()));
        holder.date.setText(String.valueOf("Date : "+postModel.getApplicableDate()));
        holder.maxTemperature.setText(String.valueOf("Max Temp. : "+ postModel.getMaxTemp()+"°"));
        holder.minTemperature.setText(String.valueOf("Min Temp. : "+postModel.getMinTemp()+"°"));
        String image=postModel.getWeatherStateAbbr();
        holder.weatherimage.setImageResource(context.getResources().getIdentifier(image,"drawable",context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return consolidatedWeatherArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView maxTemperature;
        TextView minTemperature;
        TextView windSpeed;
        TextView date;
        ImageView weatherimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            weatherimage=itemView.findViewById(R.id.weather_imageview);
            minTemperature=itemView.findViewById(R.id.min_temperature);
            maxTemperature=itemView.findViewById(R.id.max_temperature);
            date=itemView.findViewById(R.id.date);
            windSpeed=itemView.findViewById(R.id.humidity);
        }
    }
}
