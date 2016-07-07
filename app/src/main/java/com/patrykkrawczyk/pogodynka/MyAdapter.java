package com.patrykkrawczyk.pogodynka;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.malinskiy.superrecyclerview.swipe.SwipeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import me.grantland.widget.AutofitHelper;
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
        @BindView(R.id.currentTemperatureTextView) AutofitTextView currentTemperature;
        @BindView(R.id.cityNameTextView) AutofitTextView cityName;

        public OverallViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class BaseViewHolder extends BaseSwipeAdapter.BaseSwipeableViewHolder {
        @Nullable @BindView(R.id.behindCityNameTextView) AutofitTextView cityName;
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
            baseViewHolder.cityName.setText(baseViewHolder.city.getCityName());
            baseViewHolder.behindButtonsInterface = baseViewHolder.city;
            baseViewHolder.listingButtonInterface = baseViewHolder.city;

            if (baseViewHolder instanceof OverallViewHolder) {
                OverallViewHolder viewHolder = (OverallViewHolder) baseViewHolder;
                viewHolder.cityName.setText(baseViewHolder.city.getCityName());
                viewHolder.currentTemperature.setText(baseViewHolder.city.getCurrentTemperature() + "°C");
                int color = getColorFromTemperature(baseViewHolder.city.getCurrentTemperature());
                viewHolder.currentTemperature.setTextColor(color);
            } else {
                DetailsViewHolder viewHolder = (DetailsViewHolder) baseViewHolder;
                viewHolder.cityName.setText(baseViewHolder.city.getCityName());
                //viewHolder.cityName.setText(baseViewHolder.singleCityHolder.getCityName());
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
        int temp = Integer.valueOf(temperature);

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
