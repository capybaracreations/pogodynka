package com.patrykkrawczyk.pogodynka.listings;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.patrykkrawczyk.pogodynka.CitiesList;
import com.patrykkrawczyk.pogodynka.R;
import com.patrykkrawczyk.pogodynka.SingleCityHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MyAdapter extends RecyclerSwipeAdapter<MyAdapter.SwipeViewHolder> {

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static class WeatherCardViewHolder extends SwipeViewHolder {
        //@BindView(R.id.swipe) SwipeLayout swipeLayout;

        public WeatherCardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class AddCityCardViewHolder extends SwipeViewHolder {
        //@BindView(R.id.info_text) TextView info;

        public AddCityCardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class SwipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.swipe) SwipeLayout swipeLayout;
        @BindView(R.id.removeListing) ImageView removeButton;

        public SwipeViewHolder(View view) {
            super(view);
            ButterKnife.bind(view);
        }
    }

    private final int WEATHER_LISTING = 0;
    private final int ADD_CITY_LISTING = 1;


    private Context context;
    private CitiesList cities;


    public MyAdapter(Context context, CitiesList cities) {
        this.context = context;
        this.cities = cities;
    }

    @Override
    public SwipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == WEATHER_LISTING) {
            View view = LayoutInflater.from(context).inflate(R.layout.weather_card, parent, false);
            return new WeatherCardViewHolder(view);
        } else if (viewType == ADD_CITY_LISTING) {
            View view = LayoutInflater.from(context).inflate(R.layout.add_city_card, parent, false);
            return new AddCityCardViewHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        SingleCityHolder singleCityHolder = cities.get(position);
        Listing listing = singleCityHolder.produceWeatherListing();
        if (listing instanceof WeatherListing) return WEATHER_LISTING;
        // else if (cities.get(position) instanceof AddCityListing) return ADD_CITY_LISTING;
        else return -1;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SwipeViewHolder viewHolder, final int position) {
        //viewHolder.info.setText(String.valueOf(position));

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(viewHolder.removeButton);
            }
        });

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                cities.remove(position);
                notifyItemRemoved(position);
                //notifyItemRangeChanged(position, cities.size());
                mItemManger.closeAllItems();
            }
        });

        //SingleCityHolder singleCityHolder = cities.get(position);
        mItemManger.bindView(viewHolder.itemView, position);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cities.size();
    }
}
