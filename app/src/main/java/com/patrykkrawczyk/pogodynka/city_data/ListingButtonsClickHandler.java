package com.patrykkrawczyk.pogodynka.city_data;

import android.view.View;

import com.patrykkrawczyk.pogodynka.listings.BehindButtonsInterface;
import com.patrykkrawczyk.pogodynka.listings.ListingButtonInterface;
import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder.Status;

/**
 * Created by Patryk Krawczyk on 08.07.2016.
 */
public class ListingButtonsClickHandler implements ListingButtonInterface, BehindButtonsInterface {

    SingleCityHolder singleCityHolder;

    private ListingButtonsClickHandler() {}

    public ListingButtonsClickHandler(SingleCityHolder singleCityHolder) {
        this.singleCityHolder = singleCityHolder;
    }

    @Override
    public void onClickFirstWeatherCell(View view) {
        singleCityHolder.setStatus(Status.DETAILS);
        singleCityHolder.dayDetailsPicked = 0;
    }

    @Override
    public void onClickSecondWeatherCell(View view) {
        singleCityHolder.setStatus(Status.DETAILS);
        singleCityHolder.dayDetailsPicked = 1;
    }

    @Override
    public void onClickThirdWeatherCell(View view) {
        singleCityHolder.setStatus(Status.DETAILS);
        singleCityHolder.dayDetailsPicked = 2;
    }

    @Override
    public void onClickRefreshButton(View view) {
        singleCityHolder.update();
    }

    @Override
    public void onClickDeleteButton(View view) {
        singleCityHolder.parent.remove(this);
    }

}
