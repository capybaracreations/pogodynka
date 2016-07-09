package com.patrykkrawczyk.pogodynka.city_data;

import com.patrykkrawczyk.pogodynka.CitiesList;
import com.patrykkrawczyk.pogodynka.json.autocomplete.RESULT;
import com.patrykkrawczyk.pogodynka.weather.WeatherData;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class SingleCityHolder {

    public CitiesList parent;
    public ListingButtonsClickHandler listingButtonsClickHandler = new ListingButtonsClickHandler(this);

    public enum Status { NEW, UPDATING, OVERALL, DETAILS_ONE, DETAILS_TWO, DETAILS_THREE };
    private Status status = Status.NEW;

    private String name;
    private String latitude;
    private String longitude;
    public WeatherData data = new WeatherData(this);

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

        data.updateFromApi();
        //forecastIOHandler.updateFromApi();
    }

    public void setStatus(Status status) {
        this.status = status;
        notifyAdapter();
    }

    public Status getStatus() {
        return status;
    }

    public String getCityName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


    private void notifyAdapter() {
        parent.adapter.notifyItemChanged(this);
    }


    private SingleCityHolder(){}

}
