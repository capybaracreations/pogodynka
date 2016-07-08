
package com.patrykkrawczyk.pogodynka.json.wunder;

import java.util.ArrayList;
import java.util.List;

public class WunderHourlyResult {

    private Response response;
    private List<HourlyForecast> hourly_forecast = new ArrayList<HourlyForecast>();

    /**
     * 
     * @return
     *     The response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * 
     * @param response
     *     The response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * 
     * @return
     *     The hourlyForecast
     */
    public List<HourlyForecast> getHourlyForecast() {
        return hourly_forecast;
    }

    /**
     * 
     * @param hourlyForecast
     *     The hourly_forecast
     */
    public void setHourlyForecast(List<HourlyForecast> hourlyForecast) {
        this.hourly_forecast = hourlyForecast;
    }

}
