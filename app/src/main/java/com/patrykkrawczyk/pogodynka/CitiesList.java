package com.patrykkrawczyk.pogodynka;

import android.view.View;

import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder;
import com.patrykkrawczyk.pogodynka.listings.MyAdapter;

import java.util.ArrayList;


/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class CitiesList extends ArrayList<SingleCityHolder> {

    transient private MyAdapter adapter;

    @Override
    public boolean add(SingleCityHolder object) {
        object.parent = this;
        super.add(object);
        adapter.notifyItemInserted(size()-1);
        if (adapter != null) adapter.displaySnackbar(object.getCityName() + " added.", null, null);

        return true;
    }

    @Override
    public boolean remove(final Object object) {
        SingleCityHolder city = (SingleCityHolder) object;
        final int pos = indexOf(city);
        super.remove(object);
        adapter.notifyItemRemoved(pos);

        adapter.displaySnackbar(city.getCityName() + " removed.", "UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(pos, (SingleCityHolder) object);
                adapter.notifyItemInserted(pos);
            }
        });

        saveForPersistence();

        return true;
    }

    private void saveForPersistence() {
        SaveLoadManager.saveCities(adapter.context, this);
    }

    public void updateAll() {
        displaySnackbar("Refreshing all cities.");
        for (int k = 0; k < size(); k++) get(k).update();
    }

    private void updateOne(int index) {
        get(index).update();
        displaySnackbar("Refreshing " + get(index).getCityName() + ".");
    }

    private void updateOne(SingleCityHolder object) {
        updateOne(indexOf(object));
    }

    public void setAdapter(MyAdapter adapter) {
        this.adapter = adapter;
    }

    public void notifyItemChanged(SingleCityHolder singleCityHolder) {
        adapter.notifyItemChanged(singleCityHolder);
        //if (singleCityHolder.getStatus() != SingleCityHolder.Status.UPDATING)
            saveForPersistence();
    }

    public void displaySnackbar(String message) {
        adapter.displaySnackbar(message, null, null);
    }

    public void reinitialize() {

    }
}
