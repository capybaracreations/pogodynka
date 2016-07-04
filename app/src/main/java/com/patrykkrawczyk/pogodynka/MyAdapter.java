package com.patrykkrawczyk.pogodynka;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MyAdapter extends RecyclerSwipeAdapter<MyAdapter.SwipeViewHolder> {

    public void notifyItemChanged(SingleCityHolder cityHolder) {
        int position = cities.indexOf(cityHolder);
        notifyItemChanged(position);
    }

    public static class SwipeViewHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.swipe) SwipeLayout swipeLayout;
        @Nullable @BindView(R.id.removeListing) ImageView removeButton;
        @Nullable @BindView(R.id.backListing) ImageView backButon;
        @Nullable @BindView(R.id.firstWeatherCell) LinearLayout firstWeatherCell;
        @Nullable @BindView(R.id.secondWeatherCell) LinearLayout secondWeatherCell;
        @Nullable @BindView(R.id.thirdWeatherCell) LinearLayout thirdWeatherCell;
        @Nullable @BindView(R.id.cityNameTextView) TextView cityName;
        @Nullable @BindView(R.id.currentTemperatureTextView) TextView currentTemperature;

        SingleCityHolder singleCityHolder;

        public SwipeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

/*
        @Optional @OnClick(R.id.secondWeatherCell)
        public void onClickSecondWeatherCell(View view) {
            buttonInterface.onClickSecondWeatherCell();

        }*/
    }

    private Context context;
    private CitiesList cities;

    public MyAdapter(Context context, CitiesList cities) {
        this.context = context;
        this.cities = cities;
        cities.adapter = this;
    }

    @Override
    public SwipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;

        switch (SingleCityHolder.Status.values()[viewType]) {
            case DETAILS_ONE :
            case DETAILS_TWO :
            case DETAILS_THREE : layout = R.layout.details_card; break;
            case OVERALL: layout = R.layout.overall_card; break;
            default : layout = R.layout.updating_card; break;
        }

        return new SwipeViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return cities.get(position).getStatus().ordinal();
    }

    @Override
    public void onBindViewHolder(final SwipeViewHolder viewHolder, final int position) {
        if (viewHolder.swipeLayout != null) {
            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    if (viewHolder.removeButton != null)
                        YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(viewHolder.removeButton);
                    else if (viewHolder.backButon != null)
                        YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(viewHolder.backButon);
                }
            });

            mItemManger.bindView(viewHolder.itemView, position);
        }

        viewHolder.singleCityHolder = cities.get(position);

        if (viewHolder.singleCityHolder.getStatus() == SingleCityHolder.Status.OVERALL) {
            viewHolder.firstWeatherCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.singleCityHolder.onClickFirstWeatherCell();
                    notifyItemChanged(position);
                }
            });

            viewHolder.secondWeatherCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.singleCityHolder.onClickSecondWeatherCell();
                    notifyItemChanged(position);
                }
            });

            viewHolder.thirdWeatherCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.singleCityHolder.onClickThirdWeatherCell();
                    notifyItemChanged(position);
                }
            });

            viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.singleCityHolder.onClickRemoveButton();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                    cities.remove(position);
                    //notifyItemRemoved(position);
                    //notifyItemRangeChanged(position, cities.size());
                    notifyDataSetChanged();
                    mItemManger.closeAllItems();
                }
            });

            viewHolder.cityName.setText(viewHolder.singleCityHolder.getCityName());
            viewHolder.currentTemperature.setText(viewHolder.singleCityHolder.getCurrentTemperature());
        } else if (viewHolder.singleCityHolder.getStatus() != SingleCityHolder.Status.UPDATING) {
            viewHolder.backButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.singleCityHolder.onClickBackButton();
                    notifyItemChanged(position);
                    Toast.makeText(context, "Backed", Toast.LENGTH_SHORT).show();
                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                    notifyDataSetChanged();
                    mItemManger.closeAllItems();
                }
            });
        } else {
            viewHolder.cityName.setText(viewHolder.singleCityHolder.getCityName());
        }

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

}
