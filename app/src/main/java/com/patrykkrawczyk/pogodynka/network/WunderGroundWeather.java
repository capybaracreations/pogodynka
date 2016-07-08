package com.patrykkrawczyk.pogodynka.network;


import com.patrykkrawczyk.pogodynka.json.autocomplete.AutoCompleteResult;
import com.patrykkrawczyk.pogodynka.json.wunder.WunderHourlyResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public interface WunderGroundWeather {

    String URL = "http://api.wunderground.com";
    String KEY = "37ada175c0113d52";

    // http://api.wunderground.com/api/37ada175c0113d52/hourly10day/q/37.776289,-122.395234.json
    @GET("/api/{key}/hourly10day/q/{latitude},{longitude}.json")
    Call<WunderHourlyResult> hourlyForecast (@Path("key") String key, @Path("latitude") String latitude, @Path("longitude") String longitude);
}
