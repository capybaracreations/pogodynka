package com.patrykkrawczyk.pogodynka;

import android.view.View;

import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder;
import com.patrykkrawczyk.pogodynka.listings.MyAdapter;

import java.util.ArrayList;


/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class CitiesList extends ArrayList<SingleCityHolder>{

    public MyAdapter adapter;

    @Override
    public boolean add(SingleCityHolder object) {
        object.parent = this;
        super.add(object);
        //adapter.notifyItemInserted(size()-1);
        //adapter.displaySnackbar(object.getCityName() + " added.", null, null);
        object.update();
        return true;
    }

    @Override
    public boolean remove(final Object object) {
        final int pos = indexOf(object);
        //super.remove(object);
        //adapter.notifyItemRemoved(pos);
        adapter.displaySnackbar(((SingleCityHolder)object).getCityName() + " removed.", "UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add(pos, (SingleCityHolder) object);
                //adapter.notifyItemInserted(pos);
            }
        });
        return true;
    }

    public void updateAll() {
        for (int k = 0; k < size(); k++) {
            updateOne(k);
        }
    }

    private void updateOne(int index) {
        get(index).update();
    }

    private void updateOne(SingleCityHolder object) {
        updateOne(indexOf(object));
    }
}
