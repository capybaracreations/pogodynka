package com.patrykkrawczyk.pogodynka;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.dift.ui.SwipeToAction;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BaseViewHolder> implements SwipeToAction.SwipeListener<SingleCityHolder> {

    @Override
    public boolean swipeLeft(final SingleCityHolder itemData) {
        cities.remove(itemData);
        Log.d("patryczek", "swipe left");
        return true;
    }

    @Override
    public boolean swipeRight(SingleCityHolder itemData) {
        displaySnackbar(itemData.getCityName() + " loved", null, null);
        Log.d("patryczek", "swipeRigth");
        return true;
    }

    @Override
    public void onClick(SingleCityHolder itemData) {
        displaySnackbar(itemData.getCityName() + " clicked", null, null);

        Log.d("patryczek", "onclick");
    }

    @Override
    public void onLongClick(SingleCityHolder itemData) {
        displaySnackbar(itemData.getCityName() + " long clicked", null, null);

        Log.d("patryczek", "onLongclick");
    }

    public static class DetailsViewHolder extends BaseViewHolder {
        @BindView(R.id.currentTemperatureTextView) TextView currentTemperature;
        @BindView(R.id.cityNameTextView) TextView cityName;

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
        }
    }

    public static class OverallViewHolder extends BaseViewHolder {
        @BindView(R.id.currentTemperatureTextView) TextView currentTemperature;
        @BindView(R.id.cityNameTextView)  TextView     cityName;
        @BindView(R.id.firstWeatherCell)  LinearLayout firstWeatherCell;
        @BindView(R.id.secondWeatherCell) LinearLayout secondWeatherCell;
        @BindView(R.id.thirdWeatherCell)  LinearLayout thirdWeatherCell;

        public OverallViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.firstWeatherCell)
        public void onClickFirstWeatherCell(View v) {
            data.onClickFirstWeatherCell();
        }

        @OnClick(R.id.secondWeatherCell)
        public void onClickSecondWeatherCell(View v) {
            data.onClickSecondWeatherCell();
        }

        @OnClick(R.id.thirdWeatherCell)
        public void onClickThirdWeatherCell(View v) {
            data.onClickThirdWeatherCell();
        }
    }

    public static class BaseViewHolder extends SwipeToAction.ViewHolder<SingleCityHolder> {

        public BaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
            case DETAILS_THREE : viewHolder = new DetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.details_card, parent, false)); break;
            case OVERALL: viewHolder = new OverallViewHolder(LayoutInflater.from(context).inflate(R.layout.overall_card, parent, false)); break;
            default : viewHolder = new UpdateViewHolder(LayoutInflater.from(context).inflate(R.layout.update_card, parent, false)); break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return cities.get(position).getStatus().ordinal();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, final int position) {
        baseViewHolder.data = cities.get(position);

        if (baseViewHolder instanceof OverallViewHolder) {
            OverallViewHolder viewHolder = (OverallViewHolder) baseViewHolder;
            viewHolder.cityName.setText(baseViewHolder.data.getCityName());
            viewHolder.currentTemperature.setText(baseViewHolder.data.getCurrentTemperature());
        } else if (baseViewHolder instanceof UpdateViewHolder) {
            UpdateViewHolder viewHolder = (UpdateViewHolder) baseViewHolder;
            viewHolder.cityName.setText(baseViewHolder.data.getCityName());
        } else {
            DetailsViewHolder viewHolder = (DetailsViewHolder) baseViewHolder;
            //viewHolder.cityName.setText(baseViewHolder.singleCityHolder.getCityName());
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
}
