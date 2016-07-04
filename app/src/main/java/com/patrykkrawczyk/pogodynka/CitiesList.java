package com.patrykkrawczyk.pogodynka;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class CitiesList extends ArrayList<SingleCityHolder> {

    public MyAdapter adapter;

    @Override
    public boolean add(SingleCityHolder object) {
        object.parent = this;
        return super.add(object);
    }

    public void updateAll() {
        for (int k = 0; k < size(); k++) {
            get(k).updateCity();
        }
    }

    public void updateStatus(int index, SingleCityHolder.Status status) {
        get(index).setStatus(status);
    }
}
