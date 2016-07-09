package com.patrykkrawczyk.pogodynka.weather;

import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder;
import com.patrykkrawczyk.pogodynka.city_data.SingleDay;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Patryk Krawczyk on 09.07.2016.
 */
public class WeatherData {

    private SingleCityHolder singleCityHolder;

    public WunderHourlyResultHandler wunderHandler = new WunderHourlyResultHandler(this);
    public ForecastIOHourlyResultHandler forecastIOHandler = new ForecastIOHourlyResultHandler(this);

    public Double currentTemperature;
    public Double wunderCurrentTemperature;
    public Double forecastIOCurrentTemperature;
    public SingleHour.Conditions currentConditions;
    public SingleHour.Conditions wunderCurrentConditions;
    public SingleHour.Conditions forecastIOCurrentConditions;

    public List<SingleDay> averagedDays   = new ArrayList<>();
    public List<SingleDay> wunderDays     = new ArrayList<>();
    public List<SingleDay> forecastIODays = new ArrayList<>();

    public WeatherData(SingleCityHolder singleCityHolder) {
        this.singleCityHolder = singleCityHolder;
    }

    private void recalculateCurrentTemperature() {
        double average = 0;

        average += wunderCurrentTemperature;
        average += forecastIOCurrentTemperature;

        this.currentTemperature = average / 2;
    }

    public void recalculateAverages() {
        if (!wunderHandler.refreshing && !forecastIOHandler.refreshing) {
            if (wunderHandler.populated && forecastIOHandler.populated) {
                initializeAveragedDays();

                recalculateCurrentTemperature();
                recalculateCurrentConditions();
                recalculateDailyAverages();
            } else if (wunderHandler.populated && !wunderHandler.refreshing) {
                averagedDays = wunderDays;
                currentTemperature = wunderCurrentTemperature;
                currentConditions = wunderCurrentConditions;
            } else if (forecastIOHandler.populated && !forecastIOHandler.refreshing) {
                averagedDays = forecastIODays;
                currentTemperature = forecastIOCurrentTemperature;
                currentConditions = forecastIOCurrentConditions;
            } else {
                singleCityHolder.setStatus(SingleCityHolder.Status.NEW); // TODO: add remove state and dispose
                return;
            }

            singleCityHolder.setStatus(SingleCityHolder.Status.OVERALL);
        }
    }

    private void recalculateCurrentConditions() {
        int max = Math.max(wunderCurrentConditions.ordinal(), forecastIOCurrentConditions.ordinal());
        currentConditions = SingleHour.Conditions.values()[max];
    }

    private void initializeAveragedDays() {
        averagedDays.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        for (int k = 0; k < 3; k++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            averagedDays.add(new SingleDay(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                    String.valueOf(calendar.get(Calendar.MONTH) + 1),
                    String.valueOf(calendar.get(Calendar.YEAR))));
        }

    }

    private void recalculateDailyAverages() {
        int[] averageCondition = new int[SingleHour.Conditions.values().length];

        for (int k = 0; k < 3; k++) { // 3 days
            SingleDay day = averagedDays.get(k);
            SingleDay wunder = wunderDays.get(k);
            SingleDay forecastIO = forecastIODays.get(k);

            for (int m = 0; m < 24; m++) { // 24 hours
                SingleHour wHour = wunder.hours.get(m);
                SingleHour fHour = forecastIO.hours.get(m);

                Double averageTemperature = (wHour.temperature + fHour.temperature) / 2;
                averageCondition[wHour.getConditions().ordinal()]++;
                averageCondition[fHour.getConditions().ordinal()]++;

                SingleHour hour = new SingleHour(String.valueOf(m), averageTemperature);

                day.hours.add(hour);
            }

            // Count average temperature for each day
            double average = 0;
            for (SingleHour hour : day.hours)
                average += Double.valueOf(hour.temperature);
            day.setAverage(average / 24);

            // Count averaged conditions for each day
            int maxIndex = 0;
            for (int i = 0; i < averageCondition.length - 1; i++)
                if (averageCondition[i] < averageCondition[i+1]) maxIndex = i+1;
            day.setAverageConditions(SingleHour.Conditions.values()[maxIndex]);
        }
    }



    public void updateFromApi() {
        wunderHandler.updateFromApi();
        //forecastIOHandler.updateFromApi();
    }

    private WeatherData() {}

    public String getLongitude() {
        return singleCityHolder.getLongitude();
    }

    public String getLatitude() {
        return singleCityHolder.getLatitude();
    }
}
