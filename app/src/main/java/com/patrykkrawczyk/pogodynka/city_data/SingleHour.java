package com.patrykkrawczyk.pogodynka.city_data;

/**
 * Created by Patryk Krawczyk on 07.07.2016.
 */
public class SingleHour {
    public enum Conditions {CLEAR, CLOUDY, STORM, RAIN, FOG, SNOW};
    private Conditions conditions = Conditions.CLEAR;

    public String hour;
    public Double temperature;

    public SingleHour(String hour, Double temperature) {
        this.hour = hour;
        this.temperature = temperature;
    }

    public Conditions getConditions() {
        return conditions;
    }
    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
    }
}
