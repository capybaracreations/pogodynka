package com.patrykkrawczyk.pogodynka.listings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.chart.view.LineChartView;
import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.patrykkrawczyk.pogodynka.CitiesList;
import com.patrykkrawczyk.pogodynka.MainActivity;
import com.patrykkrawczyk.pogodynka.R;
import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder;
import com.patrykkrawczyk.pogodynka.city_data.SingleDay;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import me.grantland.widget.AutofitTextView;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MyAdapter extends BaseSwipeAdapter<MyAdapter.BaseViewHolder> {

    public static class UpdateViewHolder extends BaseViewHolder {
        @BindView(R.id.cityNameTextView) TextView cityName;

        public UpdateViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            swipeLayout.setSwipeEnabled(false);
        }
    }

    public static class DetailsViewHolder extends BaseViewHolder {
        @BindView(R.id.cityNameTextView)    AutofitTextView cityName;
        @BindView(R.id.chartView)           LineChartView lineChartView;

        private DetailsButtonsInterface     detailsButtonsInterface;
        private ChartHandler                chartHandler;

        public DetailsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            chartHandler = new ChartHandler(lineChartView);
            detailsButtonsInterface = chartHandler;
        }

        @Optional @OnClick (R.id.leftButton)
        public void onClickLeftButton(View view) {
            detailsButtonsInterface.onClickLeftButton(view);
        }

        @Optional @OnClick (R.id.rightButton)
        public void onClickRightButton(View view) {
            detailsButtonsInterface.onClickRightButton(view);
        }

        @Optional @OnClick (R.id.backButton)
        public void onClickBackButton(View view) {
            chartHandler.clear();
            city.setStatus(SingleCityHolder.Status.OVERALL);
        }
    }

    public static class OverallViewHolder extends BaseViewHolder {
        @BindView(R.id.cityNameTextView)             AutofitTextView cityName;
        @BindView(R.id.currentConditionsImageView)   ImageView       currentConditions;
        @BindView(R.id.currentTemperatureTextView)   AutofitTextView currentTemperature;
        @BindView(R.id.firstWeatherCellTemperature)  AutofitTextView firstWeatherCellTemperature;
        @BindView(R.id.firstWeatherCellConditions)   ImageView       firstWeatherCellConditions;
        @BindView(R.id.firstWeatherCellDate)         AutofitTextView firstWeatherCellDate;
        @BindView(R.id.secondWeatherCellTemperature) AutofitTextView secondWeatherCellTemperature;
        @BindView(R.id.secondWeatherCellConditions)  ImageView       secondWeatherCellConditions;
        @BindView(R.id.secondWeatherCellDate)        AutofitTextView secondWeatherCellDate;
        @BindView(R.id.thirdWeatherCellTemperature)  AutofitTextView thirdWeatherCellTemperature;
        @BindView(R.id.thirdWeatherCellConditions)   ImageView       thirdWeatherCellConditions;
        @BindView(R.id.thirdWeatherCellDate)         AutofitTextView thirdWeatherCellDate;


        public OverallViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class BaseViewHolder extends BaseSwipeAdapter.BaseSwipeableViewHolder {
        @Nullable @BindView(R.id.behindCityNameTextView) AutofitTextView behindCityName;
        @Nullable @BindView(R.id.behindFirstCellDay)     AutofitTextView behindFirstCellDay;
        @Nullable @BindView(R.id.behindFirstCellDate)    AutofitTextView behindFirstCellDate;
        @Nullable @BindView(R.id.behindSecondCellDay)    AutofitTextView behindSecondCellDay;
        @Nullable @BindView(R.id.behindSecondCellDate)   AutofitTextView behindSecondCellDate;
        @Nullable @BindView(R.id.behindThirdCellDay)     AutofitTextView behindThirdCellDay;
        @Nullable @BindView(R.id.behindThirdCellDate)    AutofitTextView behindThirdCellDate;

        public SingleCityHolder city;
        private BehindButtonsInterface behindButtonsInterface;
        private ListingButtonInterface listingButtonInterface;

        public BaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Optional @OnClick (R.id.refreshButton)
        public void onClickRefreshButton(View view) {
            behindButtonsInterface.onClickRefreshButton(view);
        }

        @Optional @OnClick (R.id.deleteButton)
        public void onClickDeleteButton(View view) {
            behindButtonsInterface.onClickDeleteButton(view);
        }

        @Optional @OnClick (R.id.firstWeatherCell)
        public void onClickFirstWeatherCell(View view) {
            listingButtonInterface.onClickFirstWeatherCell(view);
        }

        @Optional @OnClick (R.id.secondWeatherCell)
        public void onClickSecondWeatherCell(View view) {
            listingButtonInterface.onClickSecondWeatherCell(view);
        }

        @Optional @OnClick (R.id.thirdWeatherCell)
        public void onClickThirdWeatherCell(View view) {
            listingButtonInterface.onClickThirdWeatherCell(view);
        }

    }

    public Activity context;
    private CitiesList cities;

    public MyAdapter(Activity context, CitiesList cities) {
        this.context = context;
        this.cities = cities;
        cities.setAdapter(this);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;

        switch (SingleCityHolder.Status.values()[viewType]) {
            case DETAILS: viewHolder = new DetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_layout_details, parent, false)); break;
            case OVERALL: viewHolder = new OverallViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_layout_overall, parent, false)); break;
            default :     viewHolder = new UpdateViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_layout_updating, parent, false)); break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return cities.get(position).getStatus().ordinal();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, final int position) {
        super.onBindViewHolder(baseViewHolder, position);

        closeItem(position);
        baseViewHolder.city = cities.get(position);

        if (baseViewHolder instanceof UpdateViewHolder) {
            UpdateViewHolder viewHolder = (UpdateViewHolder) baseViewHolder;
            viewHolder.cityName.setText(baseViewHolder.city.getCityName());
        } else {
            baseViewHolder.behindCityName.setText(baseViewHolder.city.getCityName());
            baseViewHolder.behindButtonsInterface = baseViewHolder.city.listingButtonsClickHandler;
            baseViewHolder.listingButtonInterface = baseViewHolder.city.listingButtonsClickHandler;

            SingleDay day = baseViewHolder.city.data.averagedDays.get(0);
            int color = getColorFromTemperature(context, day.getAverageTemperature());
            baseViewHolder.behindFirstCellDay.setTextColor(color);
            baseViewHolder.behindFirstCellDate.setTextColor(color);
            baseViewHolder.behindFirstCellDay.setText(day.getDay());
            baseViewHolder.behindFirstCellDate.setText(day.getDate());

            day = baseViewHolder.city.data.averagedDays.get(1);
            color = getColorFromTemperature(context, day.getAverageTemperature());
            baseViewHolder.behindSecondCellDay.setTextColor(color);
            baseViewHolder.behindSecondCellDate.setTextColor(color);
            baseViewHolder.behindSecondCellDay.setText(day.getDay());
            baseViewHolder.behindSecondCellDate.setText(day.getDate());

            day = baseViewHolder.city.data.averagedDays.get(2);
            color = getColorFromTemperature(context, day.getAverageTemperature());
            baseViewHolder.behindThirdCellDay.setTextColor(color);
            baseViewHolder.behindThirdCellDate.setTextColor(color);
            baseViewHolder.behindThirdCellDay.setText(day.getDay());
            baseViewHolder.behindThirdCellDate.setText(day.getDate());

            if (baseViewHolder instanceof OverallViewHolder) {
                OverallViewHolder viewHolder = (OverallViewHolder) baseViewHolder;
                viewHolder.cityName.setText(baseViewHolder.city.getCityName());
                viewHolder.currentConditions.setImageDrawable(getDrawableFromConditions(context, viewHolder.city.data.currentConditions));

                viewHolder.currentTemperature.setText(String.format("%.0f", baseViewHolder.city.data.currentTemperature) + "°C");
                color = getColorFromTemperature(context, baseViewHolder.city.data.currentTemperature);
                viewHolder.currentTemperature.setTextColor(color);

                day = baseViewHolder.city.data.averagedDays.get(0);
                color = getColorFromTemperature(context, day.getAverageTemperature());
                viewHolder.firstWeatherCellTemperature.setTextColor(color);
                viewHolder.firstWeatherCellTemperature.setText(day.getAverageTemperatureString() + "°C");
                viewHolder.firstWeatherCellConditions.setImageDrawable(getDrawableFromConditions(context, day.getAverageConditions()));
                viewHolder.firstWeatherCellDate.setText(day.getDate());

                day = baseViewHolder.city.data.averagedDays.get(1);
                color = getColorFromTemperature(context, day.getAverageTemperature());
                viewHolder.secondWeatherCellTemperature.setTextColor(color);
                viewHolder.secondWeatherCellTemperature.setText(day.getAverageTemperatureString() + "°C");
                viewHolder.secondWeatherCellConditions.setImageDrawable(getDrawableFromConditions(context, day.getAverageConditions()));
                viewHolder.secondWeatherCellDate.setText(day.getDate());

                day = baseViewHolder.city.data.averagedDays.get(2);
                color = getColorFromTemperature(context, day.getAverageTemperature());
                viewHolder.thirdWeatherCellTemperature.setTextColor(color);
                viewHolder.thirdWeatherCellTemperature.setText(day.getAverageTemperatureString() + "°C");
                viewHolder.thirdWeatherCellConditions.setImageDrawable(getDrawableFromConditions(context, day.getAverageConditions()));
                viewHolder.thirdWeatherCellDate.setText(day.getDate());

            } else {
                DetailsViewHolder viewHolder = (DetailsViewHolder) baseViewHolder;
                day = baseViewHolder.city.data.averagedDays.get(baseViewHolder.city.dayDetailsPicked);

                viewHolder.chartHandler.initializeHours(day.hours);
                viewHolder.cityName.setText(baseViewHolder.city.getCityName());

            }
        }

    }

    public static Drawable getDrawableFromConditions(Context context, SingleHour.Conditions conditions) {
        int resource;

        switch (conditions) {
            case CLEAR  : resource = R.drawable.clear;  break;
            case CLOUDY : resource = R.drawable.cloudy; break;
            case RAIN   : resource = R.drawable.rain;   break;
            case FOG    : resource = R.drawable.fog;    break;
            case SNOW   : resource = R.drawable.snow;   break;
            case STORM  : resource = R.drawable.storm;   break;
            default     : resource = R.drawable.clear;  break;
        }

        Drawable drawable = context.getResources().getDrawable(resource);
        return drawable;
    }

    public void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(context.findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(context.getResources().getColor(R.color.hot));

        snack.show();
    }



    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void notifyItemChanged(SingleCityHolder cityHolder) {
        final int position = cities.indexOf(cityHolder);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(position);
            }
        });
    }

    public static int getColorFromTemperature(Context context, Double temperature) {
        int color = 0;
        double temp = temperature;

        if (temp > 20) {
            color = context.getResources().getColor(R.color.hot);
        } else if (temp < 20) {
            color = context.getResources().getColor(R.color.cold);
        } else {
            color = context.getResources().getColor(R.color.neutral);
        }

        return color;
    }
}
