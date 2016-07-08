package com.patrykkrawczyk.pogodynka.city_data;

/**
 * Created by Patryk Krawczyk on 07.07.2016.
 */
public class SingleHour {
    String hour;
    String temperature;

    private SingleHour() {}
    public SingleHour(String hour, String temperature) {
        this.hour = hour;
        this.temperature = temperature;
    }
}
