package com.patrykkrawczyk.pogodynka.city_data;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.patrykkrawczyk.pogodynka.CitiesList;
import com.patrykkrawczyk.pogodynka.json.autocomplete.AutoCompleteResult;
import com.patrykkrawczyk.pogodynka.json.wunder.FCTTIME;
import com.patrykkrawczyk.pogodynka.json.wunder.HourlyForecast;
import com.patrykkrawczyk.pogodynka.json.wunder.WunderHourlyResult;
import com.patrykkrawczyk.pogodynka.listings.BehindButtonsInterface;
import com.patrykkrawczyk.pogodynka.listings.ListingButtonInterface;
import com.patrykkrawczyk.pogodynka.json.autocomplete.RESULT;
import com.patrykkrawczyk.pogodynka.network.WunderGroundAutoComplete;
import com.patrykkrawczyk.pogodynka.network.WunderGroundWeather;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class SingleCityHolder implements ListingButtonInterface, BehindButtonsInterface, Callback<WunderHourlyResult> {


    public CitiesList parent;


    public enum Status { NEW, UPDATING, OVERALL, DETAILS_ONE, DETAILS_TWO, DETAILS_THREE };
    private Status status = Status.NEW;

    private String name;
    private String latitude;
    private String longitude;
    private String temperature;
    public List<SingleDay> days = new ArrayList<>();


    private SingleCityHolder(){}

    public SingleCityHolder(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SingleCityHolder(String name, double latitude, double longitude) {
        this(name, String.valueOf(latitude), String.valueOf(longitude));
    }

    public SingleCityHolder(RESULT result) {
        this(result.name, result.lat, result.lon);
    }

    public void update() {
        setStatus(Status.UPDATING);
        parent.adapter.displaySnackbar("Refreshing " + getCityName() + ".", null, null);

        updateFromWunder();
    }

    private void updateFromWunder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WunderGroundWeather.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WunderGroundWeather service = retrofit.create(WunderGroundWeather.class);
        Call<WunderHourlyResult> call = service.hourlyForecast(WunderGroundWeather.KEY, latitude, longitude);

        call.enqueue(this);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        notifyAdapter();
    }

    public String getCityName() {
        return name;
    }

    public String getCurrentTemperature() {
        return temperature;
    }


    private void notifyAdapter() {
        parent.adapter.notifyItemChanged(this);
    }


    @Override
    public void onResponse(Call<WunderHourlyResult> call, Response<WunderHourlyResult> response) {
        parseWunderResult(response.body());
        setStatus(Status.OVERALL);
    }


    @Override
    public void onFailure(Call<WunderHourlyResult> call, Throwable t) {

    }

    private void parseWunderResult(WunderHourlyResult result) {
        days.clear();
        List<HourlyForecast> list = result.getHourlyForecast();
        Set<String> daysFound = new HashSet<>();

        // get current day temperature
        temperature = list.get(0).getTemp().getMetric();
        String currentDay = list.get(0).getFCTTIME().getMday_padded();

        // use this instance for travering the same day
        SingleDay parsingDay = new SingleDay(list.get(0).getFCTTIME().getMday_padded(),
                                             list.get(0).getFCTTIME().getMon_padded(),
                                             list.get(0).getFCTTIME().getYear());

        for (HourlyForecast forecast : list) {

            FCTTIME time = forecast.getFCTTIME();
            String day   = time.getMday_padded();

            // skip if it's today because we only need one temperature for current day
            if (day.equals(currentDay)) continue;
            else {
                String hour  = time.getHour_padded();
                String month = time.getMon_padded();
                String year  = time.getYear();

                // Parse this hour if it belongs to a day that we are parsing or
                // we still can parse another day (we should only parse 4 days).
                // So dont parse if this is a new day (contains) and we already parsed 4 days
                if (daysFound.size() < 3) {

                    boolean isNew = daysFound.add(day);
                    if (isNew) {
                        parsingDay = new SingleDay(day, month, year);
                        days.add(parsingDay);
                    }

                    // We decided that we want to parse this hour, create it and add it to a day
                    SingleHour singleHour = new SingleHour(hour, forecast.getTemp().getMetric());
                    parsingDay.hours.add(singleHour);
                } else break; // break because we already have 4 days parsed
            }

        }

        for (SingleDay day : days) {
            double average = 0;

            for (SingleHour hour : day.hours) average += Double.valueOf(hour.temperature);
            average /= day.hours.size();

            day.setWunderAverage(String.valueOf(average));
        }
    }


    @Override
    public void onClickFirstWeatherCell(View view) {
        setStatus(Status.DETAILS_ONE);
    }

    @Override
    public void onClickSecondWeatherCell(View view) {
        setStatus(Status.DETAILS_TWO);
    }

    @Override
    public void onClickThirdWeatherCell(View view) {
        setStatus(Status.DETAILS_THREE);
    }

    @Override
    public void onClickRefreshButton(View view) {
        update();
    }

    @Override
    public void onClickDeleteButton(View view) {
        parent.remove(this);
    }

}
