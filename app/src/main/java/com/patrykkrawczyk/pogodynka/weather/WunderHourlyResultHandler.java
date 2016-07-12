package com.patrykkrawczyk.pogodynka.weather;

import com.patrykkrawczyk.pogodynka.city_data.SingleDay;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour.Conditions;
import com.patrykkrawczyk.pogodynka.json.wunder.FCTTIME;
import com.patrykkrawczyk.pogodynka.json.wunder.HourlyForecast;
import com.patrykkrawczyk.pogodynka.json.wunder.WunderHourlyResult;
import com.patrykkrawczyk.pogodynka.network.WunderGroundWeather;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.*;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk Krawczyk on 08.07.2016.
 */
public class WunderHourlyResultHandler implements Callback<WunderHourlyResult>, Serializable {

    public boolean refreshing = false;
    public boolean populated = false;
    WeatherData data;

    public WunderHourlyResultHandler(WeatherData data) {
        this.data = data;
    }

    public void updateFromApi() {
        populated = false;
        refreshing = true;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WunderGroundWeather.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WunderGroundWeather service = retrofit.create(WunderGroundWeather.class);
        Call<WunderHourlyResult> call = service.hourlyForecast(data.getLatitude(), data.getLongitude());

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<WunderHourlyResult> call, Response<WunderHourlyResult> response) {
        populated = response.body().getHourlyForecast().size() != 0;

        if (populated) parseResult(response.body());
        refreshing = false;
        data.recalculateAverages();
    }


    @Override
    public void onFailure(Call<WunderHourlyResult> call, Throwable t) {
        populated = false;
        refreshing = false;
    }


    private void parseResult(WunderHourlyResult result) {
        data.wunderDays.clear();
        List<HourlyForecast> list = result.getHourlyForecast();

        setCurrentDayWeather(list);

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
        for (HourlyForecast forecast : list) {
            FCTTIME time = forecast.getFCTTIME();
            Long epoch = Long.valueOf(time.getEpoch()) * 1000L;
            parsingDay.setTime(new Date(epoch));

            // skip if it's today because we only need one temperature for current day
            if (parsingDay.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) continue;
            else if (parsingDay.before(limit)) {

                if (hoursParsed % 24 == 0) {
                    String day  = time.getMday_padded();
                    String month = time.getMon_padded();
                    String year  = time.getYear();
                    SingleDay singleDay = new SingleDay(day, month, year);
                    data.wunderDays.add(singleDay);
                }

                String hour  = time.getHour_padded();
                SingleHour singleHour = new SingleHour(hour, forecast.getTemp().getMetric());
                singleHour.setConditions(parseIconToConditions(forecast.getIcon()));
                data.wunderDays.get(data.wunderDays.size()-1).hours.add(singleHour);

                hoursParsed++;
            } else break;
        }

        for (SingleDay day : data.wunderDays) day.calculateDayAverages();
    }

    private void setCurrentDayWeather(List<HourlyForecast> list) {
        data.wunderCurrentConditions = parseIconToConditions(list.get(0).getIcon());
        data.wunderCurrentTemperature = list.get(0).getTemp().getMetric();
    }


    public Conditions parseIconToConditions(String icon) {
        // chanceflurries, chancerain, chancesleet, chancesnow, chancetstorms,
        // clear, cloudy, hazy, mostlycloudy, mostlysunny,
        // partlycloudy, partlysunny, sleet, rain,
        // snow, sunny, tstorms
        Conditions conditions = Conditions.CLEAR;

        switch (icon) {
            case "chanceflurries"   : conditions = Conditions.SNOW;   break;
            case "chancerain"       : conditions = Conditions.RAIN;   break;
            case "chancesleet"      : conditions = Conditions.SNOW;   break;
            case "chancesnow"       : conditions = Conditions.SNOW;   break;
            case "chancetstorms"    : conditions = Conditions.STORM;   break;
            case "clear"            : conditions = Conditions.CLEAR;  break;
            case "cloudy"           : conditions = Conditions.CLOUDY; break;
            case "hazy"             : conditions = Conditions.FOG;    break;
            case "mostlycloudy"     : conditions = Conditions.CLOUDY; break;
            case "mostlysunny"      : conditions = Conditions.CLEAR;  break;
            case "partlycloudy"     : conditions = Conditions.CLOUDY; break;
            case "partlysunny"      : conditions = Conditions.CLEAR;  break;
            case "sleet"            : conditions = Conditions.SNOW;   break;
            case "rain"             : conditions = Conditions.RAIN;   break;
            case "snow"             : conditions = Conditions.SNOW;   break;
            case "sunny"            : conditions = Conditions.CLEAR;  break;
            case "tstorms"          : conditions = Conditions.STORM;   break;
            default                 : conditions = Conditions.CLEAR;  break;
        }

        return conditions;
    }


    private WunderHourlyResultHandler() {}

}
