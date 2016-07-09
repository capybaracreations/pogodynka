package com.patrykkrawczyk.pogodynka.weather;

import com.patrykkrawczyk.pogodynka.city_data.SingleDay;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour.Conditions;
import com.patrykkrawczyk.pogodynka.json.forecastio.Datum;
import com.patrykkrawczyk.pogodynka.json.forecastio.ForecastIOResult;
import com.patrykkrawczyk.pogodynka.network.ForecastIO;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk Krawczyk on 08.07.2016.
 */
public class ForecastIOHourlyResultHandler implements Callback<ForecastIOResult> {


    public boolean refreshing = false;
    public boolean populated = false;
    WeatherData data;

    private ForecastIOHourlyResultHandler() {}

    public ForecastIOHourlyResultHandler(WeatherData data) {
        this.data = data;
    }

    public void updateFromApi() {
        populated = false;
        refreshing = true;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ForecastIO.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForecastIO service = retrofit.create(ForecastIO.class);
        Call<ForecastIOResult> call = service.hourlyForecast(data.getLatitude(), data.getLongitude());

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ForecastIOResult> call, Response<ForecastIOResult> response) {parseResult(response.body());
        populated = response.body().getHourly().getData().size() != 0;

        if (populated) parseResult(response.body());
        refreshing = false;
        data.recalculateAverages();
    }

    @Override
    public void onFailure(Call<ForecastIOResult> call, Throwable t) {
        populated = false;
        refreshing = false;
    }


    private void parseResult(ForecastIOResult result) {
        data.forecastIODays.clear();

        List<Datum> hours = result.getHourly().getData();

        setCurrentDayWeather(result);

        // Set current day
        Calendar today = Calendar.getInstance();
        today.setTime(new Date(System.currentTimeMillis()));

        // Set limiting date
        Calendar limit = Calendar.getInstance();
        limit.setTime(new Date(System.currentTimeMillis()));
        limit.add(Calendar.DAY_OF_MONTH, 4);
        limit.set(Calendar.MILLISECOND, 0);
        limit.set(Calendar.SECOND, 0);
        limit.set(Calendar.MINUTE, 0);
        limit.set(Calendar.HOUR_OF_DAY, 0);

        int hoursParsed = 0;

        Calendar parsingDay = Calendar.getInstance();
        for (Datum date : hours) {

            Long epoch = Long.valueOf(date.getTime());
            parsingDay.setTime(new Date(epoch));

            // skip if it's today because we only need one temperature for current day
            if (parsingDay.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) continue;
            else if (parsingDay.before(limit)) {

                if (hoursParsed % 24 == 0) {
                    String day   = String.valueOf(parsingDay.get(Calendar.DAY_OF_MONTH));
                    String month = String.valueOf(parsingDay.get(Calendar.MONTH) + 1);
                    String year  = String.valueOf(parsingDay.get(Calendar.YEAR));
                    SingleDay singleDay = new SingleDay(day, month, year);
                    data.forecastIODays.add(singleDay);
                }

                String hour  = String.valueOf(parsingDay.get(Calendar.HOUR_OF_DAY));
                SingleHour singleHour = new SingleHour(hour, date.getTemperature());
                singleHour.setConditions(parseIconToConditions(date.getIcon()));
                data.forecastIODays.get(data.forecastIODays.size()-1).hours.add(singleHour);

                hoursParsed++;
            } else break;
        }

        for (SingleDay day : data.forecastIODays) day.calculateDayAverages();
    }

    private void setCurrentDayWeather(ForecastIOResult result) {
        data.forecastIOCurrentTemperature = result.getCurrently().getTemperature();
        data.forecastIOCurrentConditions = parseIconToConditions(result.getCurrently().getIcon());

    }

    public Conditions parseIconToConditions(String icon) {
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, partly-cloudy-night
        Conditions conditions = Conditions.CLEAR;

        switch (icon) {
            case "clear-day"            : conditions = Conditions.CLEAR;  break;
            case "clear-night"          : conditions = Conditions.CLEAR;  break;
            case "rain"                 : conditions = Conditions.RAIN;   break;
            case "snow"                 : conditions = Conditions.SNOW;   break;
            case "sleet"                : conditions = Conditions.SNOW;   break;
            case "wind"                 : conditions = Conditions.CLEAR;  break;
            case "fog"                  : conditions = Conditions.FOG;    break;
            case "cloudy"               : conditions = Conditions.CLOUDY; break;
            case "partly-cloudy-day"    : conditions = Conditions.CLOUDY; break;
            case "partly-cloudy-night"  : conditions = Conditions.CLOUDY; break;
            default                     : conditions = Conditions.CLEAR;  break;
        }

        return conditions;
    }
}
