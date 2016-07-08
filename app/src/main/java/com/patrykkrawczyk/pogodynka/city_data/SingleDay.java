package com.patrykkrawczyk.pogodynka.city_data;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk Krawczyk on 07.07.2016.
 */
public class SingleDay {
    private String day;
    private String month;
    private String year;
    private String wunderAverage = "";
    private String forecastIOAverage = "";
    private String average = "";
    public List<SingleHour> hours = new ArrayList<>();


    private SingleDay() {}

    public SingleDay(String day, String month, String year) {
        this.day   = day;
        this.month = month;
        this.year  = year;
    }

    public String getAverage() {
        return String.format("%.2f", Double.valueOf(average));
    }

    public void setWunderAverage(String wunderAverage) {
        this.wunderAverage = wunderAverage;
        recalculateAverage();
    }

    public void setForecastIOAverage(String forecastIOAverage) {
        this.forecastIOAverage = forecastIOAverage;
        recalculateAverage();
    }

    private void recalculateAverage() {
        double average = 0;
        int counter = 0;

        if (!wunderAverage.isEmpty()) {
            average += Double.valueOf(wunderAverage);
            counter++;
        }

        if (!forecastIOAverage.isEmpty()) {
            average += Double.valueOf(forecastIOAverage);
            counter++;
        }

        this.average = String.valueOf(average/counter);
    }

    public String getDay() {
        String day = "";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(this.year), Integer.valueOf(this.month)-1, Integer.valueOf(this.day));

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY    : day = "MON"; break;
            case Calendar.TUESDAY   : day = "TUE"; break;
            case Calendar.WEDNESDAY : day = "WED"; break;
            case Calendar.THURSDAY  : day = "THU"; break;
            case Calendar.FRIDAY    : day = "FRI"; break;
            case Calendar.SATURDAY  : day = "SAT"; break;
            case Calendar.SUNDAY    : day = "SUN"; break;
            default: day = "UNKNOWN";
        }

        return day;
    }

    public String getDate() {
        String date = day + "." + month;
        return date;
    }
}
