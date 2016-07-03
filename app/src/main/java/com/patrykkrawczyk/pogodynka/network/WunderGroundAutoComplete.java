package com.patrykkrawczyk.pogodynka.network;

import com.patrykkrawczyk.pogodynka.json.autocomplete.AutoCompleteResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public interface WunderGroundAutoComplete {

    String URL = "http://autocomplete.wunderground.com";

    @GET("aq")
    Call<AutoCompleteResult> findCity (@Query("query") String cityName);
}
