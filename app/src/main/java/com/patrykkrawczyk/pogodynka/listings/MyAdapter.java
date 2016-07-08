package com.patrykkrawczyk.pogodynka.listings;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.patrykkrawczyk.pogodynka.CitiesList;
import com.patrykkrawczyk.pogodynka.R;
import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder;
import com.patrykkrawczyk.pogodynka.city_data.SingleDay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import me.grantland.widget.AutofitTextView;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MyAdapter extends BaseSwipeAdapter<MyAdapter.BaseViewHolder> {

    public static class DetailsViewHolder extends BaseViewHolder {
        @BindView(R.id.cityNameTextView) AutofitTextView cityName;

        public DetailsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class UpdateViewHolder extends BaseViewHolder {
        @BindView(R.id.cityNameTextView) TextView cityName;

        public UpdateViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            swipeLayout.setSwipeEnabled(false);
        }
    }

    public static class OverallViewHolder extends BaseViewHolder {
        @BindView(R.id.cityNameTextView)             AutofitTextView cityName;
        @BindView(R.id.currentTemperatureTextView)   AutofitTextView currentTemperature;
        @BindView(R.id.firstWeatherCellTemperature)  AutofitTextView firstWeatherCellTemperature;
        @BindView(R.id.firstWeatherCellDay)          AutofitTextView firstWeatherCellDay;
        @BindView(R.id.firstWeatherCellDate)         AutofitTextView firstWeatherCellDate;
        @BindView(R.id.secondWeatherCellTemperature) AutofitTextView secondWeatherCellTemperature;
        @BindView(R.id.secondWeatherCellDay)         AutofitTextView secondWeatherCellDay;
        @BindView(R.id.secondWeatherCellDate)        AutofitTextView secondWeatherCellDate;
        @BindView(R.id.thirdWeatherCellTemperature)  AutofitTextView thirdWeatherCellTemperature;
        @BindView(R.id.thirdWeatherCellDay)          AutofitTextView thirdWeatherCellDay;
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

    private Activity context;
    private CitiesList cities;

    public MyAdapter(Activity context, CitiesList cities) {
        this.context = context;
        this.cities = cities;
        cities.adapter = this;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;

        switch (SingleCityHolder.Status.values()[viewType]) {
            case DETAILS_ONE :
            case DETAILS_TWO :
            case DETAILS_THREE : viewHolder = new DetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_layout_details, parent, false)); break;
            case OVERALL: viewHolder = new OverallViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_layout_overall, parent, false)); break;
            default : viewHolder = new UpdateViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_layout_updating, parent, false)); break;
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
            baseViewHolder.behindButtonsInterface = baseViewHolder.city;
            baseViewHolder.listingButtonInterface = baseViewHolder.city;

            SingleDay day = baseViewHolder.city.days.get(0);
            int color = getColorFromTemperature(day.getAverage());
            baseViewHolder.behindFirstCellDay.setTextColor(color);
            baseViewHolder.behindFirstCellDate.setTextColor(color);
            baseViewHolder.behindFirstCellDay.setText(day.getDay());
            baseViewHolder.behindFirstCellDate.setText(day.getDate());

            day = baseViewHolder.city.days.get(1);
            color = getColorFromTemperature(day.getAverage());
            baseViewHolder.behindSecondCellDay.setTextColor(color);
            baseViewHolder.behindSecondCellDate.setTextColor(color);
            baseViewHolder.behindSecondCellDay.setText(day.getDay());
            baseViewHolder.behindSecondCellDate.setText(day.getDate());

            day = baseViewHolder.city.days.get(2);
            color = getColorFromTemperature(day.getAverage());
            baseViewHolder.behindThirdCellDay.setTextColor(color);
            baseViewHolder.behindThirdCellDate.setTextColor(color);
            baseViewHolder.behindThirdCellDay.setText(day.getDay());
            baseViewHolder.behindThirdCellDate.setText(day.getDate());

            if (baseViewHolder instanceof OverallViewHolder) {
                OverallViewHolder viewHolder = (OverallViewHolder) baseViewHolder;
                viewHolder.cityName.setText(baseViewHolder.city.getCityName());

                viewHolder.currentTemperature.setText(baseViewHolder.city.getCurrentTemperature() + "°C");
                color = getColorFromTemperature(baseViewHolder.city.getCurrentTemperature());
                viewHolder.currentTemperature.setTextColor(color);

                day = baseViewHolder.city.days.get(0);
                color = getColorFromTemperature(day.getAverage());
                viewHolder.firstWeatherCellTemperature.setTextColor(color);
                viewHolder.firstWeatherCellTemperature.setText(day.getAverage() + "°C");
                viewHolder.firstWeatherCellDay.setText(day.getDay());
                viewHolder.firstWeatherCellDate.setText(day.getDate());

                day = baseViewHolder.city.days.get(1);
                color = getColorFromTemperature(day.getAverage());
                viewHolder.secondWeatherCellTemperature.setTextColor(color);
                viewHolder.secondWeatherCellTemperature.setText(day.getAverage() + "°C");
                viewHolder.secondWeatherCellDay.setText(day.getDay());
                viewHolder.secondWeatherCellDate.setText(day.getDate());

                day = baseViewHolder.city.days.get(2);
                color = getColorFromTemperature(day.getAverage());
                viewHolder.thirdWeatherCellTemperature.setTextColor(color);
                viewHolder.thirdWeatherCellTemperature.setText(day.getAverage() + "°C");
                viewHolder.thirdWeatherCellDay.setText(day.getDay());
                viewHolder.thirdWeatherCellDate.setText(day.getDate());

            } else {
                DetailsViewHolder viewHolder = (DetailsViewHolder) baseViewHolder;
                viewHolder.cityName.setText(baseViewHolder.city.getCityName());
                //viewHolder.behindCityName.setText(baseViewHolder.singleCityHolder.getCityName());
            }
        }

    }

    public void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(context.findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(context.getResources().getColor(R.color.rightIcon));

        snack.show();
    }



    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void notifyItemChanged(SingleCityHolder cityHolder) {
        int position = cities.indexOf(cityHolder);
        notifyItemChanged(position);
    }

    private int getColorFromTemperature(String temperature) {
        int color = 0;
        double temp = Double.valueOf(temperature);

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
