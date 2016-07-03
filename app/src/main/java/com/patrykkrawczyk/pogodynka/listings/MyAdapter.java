package com.patrykkrawczyk.pogodynka.listings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrykkrawczyk.pogodynka.CitiesList;
import com.patrykkrawczyk.pogodynka.R;
import com.patrykkrawczyk.pogodynka.SingleCityHolder;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CitiesList cities;

    private final int WEATHER_LISTING = 0;
    private final int ADD_CITY_LISTING = 1;

    public static class WeatherCardViewHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.info_text) TextView info;

        public WeatherCardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class AddCityCardViewHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.info_text) TextView info;

        public AddCityCardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public MyAdapter(CitiesList cities) {
        this.cities = cities;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == WEATHER_LISTING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_card, parent, false);
            return new WeatherCardViewHolder(view);
        }
        else if (viewType == ADD_CITY_LISTING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_city_card, parent, false);
            return new AddCityCardViewHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        SingleCityHolder singleCityHolder = cities.get(position);
        Listing listing = singleCityHolder.produceWeatherListing();
        if (listing instanceof WeatherListing) return WEATHER_LISTING;
        //else if (cities.get(position) instanceof AddCityListing) return ADD_CITY_LISTING;
        else return -1;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //viewHolder.info.setText(String.valueOf(position));
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cities.size();
    }
}
