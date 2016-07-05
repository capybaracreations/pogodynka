package com.patrykkrawczyk.pogodynka;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

import co.dift.ui.SwipeToAction;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class CitiesList extends ArrayList<SingleCityHolder>{

    public MyAdapter adapter;

    @Override
    public boolean add(SingleCityHolder object) {
        object.parent = this;
        super.add(object);
        adapter.notifyItemInserted(size()-1);
        adapter.displaySnackbar(object.getCityName() + " added.", null, null);
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
            get(k).updateCity();
        }
    }
}
