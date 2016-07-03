package com.patrykkrawczyk.pogodynka;

import com.mingle.entity.MenuEntity;
import com.patrykkrawczyk.pogodynka.json.autocomplete.RESULT;
import com.patrykkrawczyk.pogodynka.listings.WeatherListing;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class SingleCityHolder {

    public static enum Status { NEW, UPDATING, STABLE };
    private Status status = Status.NEW;

    private String name;
    private String latitude;
    private String longitude;

    private WeatherListing weatherListing;

    private SingleCityHolder(){}

    public SingleCityHolder(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SingleCityHolder(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
    }

    public SingleCityHolder(RESULT autoCompleteResultEntity) {
        this.name = autoCompleteResultEntity.name;
        this.latitude = autoCompleteResultEntity.lat;
        this.longitude = autoCompleteResultEntity.lon;
    }

    public WeatherListing produceWeatherListing() {
        if (weatherListing == null) weatherListing = new WeatherListing();
        return weatherListing;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
