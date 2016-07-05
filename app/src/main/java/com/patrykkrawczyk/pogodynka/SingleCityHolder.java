package com.patrykkrawczyk.pogodynka;

import com.patrykkrawczyk.pogodynka.json.autocomplete.RESULT;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class SingleCityHolder implements ListingButtonInterface {


    public CitiesList parent;

    @Override
    public void onClickFirstWeatherCell() {
        //setStatus(Status.DETAILS_ONE);
    }

    @Override
    public void onClickSecondWeatherCell() {
        //setStatus(Status.DETAILS_TWO);
    }

    @Override
    public void onClickThirdWeatherCell() {
        //setStatus(Status.DETAILS_THREE);
    }

    @Override
    public void onClickRemoveButton() {

    }

    @Override
    public void onClickBackButton() {
        setStatus(Status.OVERALL);
    }

    public String getCityName() {
        return name;
    }

    public String getCurrentTemperature() {
        return temperature;
    }

    public enum Status { UPDATING, OVERALL, DETAILS_ONE, DETAILS_TWO, DETAILS_THREE };
    private Status status = Status.OVERALL;

    private String name;
    private String latitude;
    private String longitude;
    private String temperature = "10";
    private CellInformation[] cells = new CellInformation[3];

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

    public void setStatus(Status status) {
        this.status = status;
        notifyAdapter();
    }

    public Status getStatus() {
        return status;
    }

    public void updateCity() {
        setStatus(Status.UPDATING);
    }

    public void finishUpdate() {
        setStatus(Status.OVERALL);
    }

    private void notifyAdapter() {
        parent.adapter.notifyItemChanged(this);
    }

    private static class CellInformation {
        String day = "";
        String date = "";
        String weather = "";

        public CellInformation(String day, String date, String weather) {
            this.day = day;
            this.date = date;
            this.weather = weather;
        }
    }
}
