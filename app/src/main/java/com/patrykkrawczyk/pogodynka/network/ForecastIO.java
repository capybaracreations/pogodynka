package com.patrykkrawczyk.pogodynka.network;

import com.patrykkrawczyk.pogodynka.json.forecastio.ForecastIOResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public interface ForecastIO {

    String URL = "https://api.forecast.io";
    String KEY = "74d3272f81d67f7330a1ce12ad3cac14";

    // https://api.forecast.io/forecast/74d3272f81d67f7330a1ce12ad3cac14/37.8267,-122.423?units=si&exclude=minutely,flags,daily&extend=hourly
    @GET("/forecast/"+KEY+"/{latitude},{longitude}?units=si&exclude=minutely,flags,daily&extend=hourly")
    Call<ForecastIOResult> hourlyForecast (@Path("latitude") String latitude, @Path("longitude") String longitude);
}
