package com.patrykkrawczyk.pogodynka.city_data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour.Conditions;

/**
 * Created by Patryk Krawczyk on 07.07.2016.
 */
public class SingleDay implements Serializable {

    private Double average;
    private Conditions averageConditions;

    private String day;
    private String month;
    private String year;

    public List<SingleHour> hours = new ArrayList<>();

    public SingleDay(String day, String month, String year) {
        this.day   = day;
        this.month = month;
        this.year  = year;
    }



    public void calculateDayAverages() {
        double average = 0;
        int[] averageCondition = new int[Conditions.values().length];

        for (SingleHour hour : hours) {
            average += Double.valueOf(hour.temperature);
            averageCondition[hour.getConditions().ordinal()]++;
        }

        average /= hours.size();
        setAverage(average);

        int maxIndex = 0;
        for (int k = 0; k < averageCondition.length - 1; k++) {
            if (averageCondition[k] < averageCondition[k+1]) maxIndex = k+1;
        }

        setAverageConditions(Conditions.values()[maxIndex]);
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
        String date = day + "." + String.format("%02d", Integer.valueOf(month)) + "." + year;;
        return date;
    }

    public String getAverageTemperatureString() {
        return String.format("%.2f", average);
    }



    public Double getAverageTemperature() {
        return average;
    }

    public Conditions getAverageConditions() {
        return averageConditions;
    }

    public void setAverageConditions(Conditions averageConditions) {
        this.averageConditions = averageConditions;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    private SingleDay() {}

}
