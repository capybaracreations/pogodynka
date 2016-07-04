package com.patrykkrawczyk.pogodynka;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BaseViewHolder> {

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
            singleCityHolder.onClickFirstWeatherCell();
        }

        @OnClick(R.id.secondWeatherCell)
        public void onClickSecondWeatherCell(View v) {
            singleCityHolder.onClickSecondWeatherCell();
        }

        @OnClick(R.id.thirdWeatherCell)
        public void onClickThirdWeatherCell(View v) {
            singleCityHolder.onClickThirdWeatherCell();
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        SingleCityHolder singleCityHolder;

        public BaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    private Context context;
    private CitiesList cities;

    public MyAdapter(Context context, CitiesList cities) {
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
            case DETAILS_THREE : viewHolder = new DetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.details_card, parent, false));
            case OVERALL: viewHolder = new OverallViewHolder(LayoutInflater.from(context).inflate(R.layout.overall_card, parent, false));
            default : viewHolder = new UpdateViewHolder(LayoutInflater.from(context).inflate(R.layout.update_card, parent, false));
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return cities.get(position).getStatus().ordinal();
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder baseViewHolder, final int position) {
        baseViewHolder.singleCityHolder = cities.get(position);

        if (baseViewHolder instanceof OverallViewHolder) {
            OverallViewHolder viewHolder = (OverallViewHolder) baseViewHolder;
            viewHolder.cityName.setText(baseViewHolder.singleCityHolder.getCityName());
            viewHolder.currentTemperature.setText(baseViewHolder.singleCityHolder.getCurrentTemperature());
        } else if (baseViewHolder instanceof UpdateViewHolder) {
            UpdateViewHolder viewHolder = (UpdateViewHolder) baseViewHolder;
            viewHolder.cityName.setText(baseViewHolder.singleCityHolder.getCityName());
        } else {
            DetailsViewHolder viewHolder = (DetailsViewHolder) baseViewHolder;
            //viewHolder.cityName.setText(baseViewHolder.singleCityHolder.getCityName());
        }

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
