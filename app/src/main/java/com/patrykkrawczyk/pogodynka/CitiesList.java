package com.patrykkrawczyk.pogodynka;

import com.patrykkrawczyk.pogodynka.json.autocomplete.RESULT;
import com.patrykkrawczyk.pogodynka.listings.Listing;

import java.util.ArrayList;

/**
 * Created by Patryk Krawczyk on 03.07.2016.
 */
public class CitiesList {
    private ArrayList<SingleCityHolder> cities = new ArrayList<>();

    public int add(SingleCityHolder singleCityHolder) {
        cities.add(singleCityHolder);
        return size() - 1;
    }

    public void remove(int index) {
        cities.remove(index);
    }

    public void remove(SingleCityHolder singleCityHolder) {
        cities.remove(singleCityHolder);
    }

    public int size() {
        return cities.size();
    }

    public SingleCityHolder get(int i) {
        return cities.get(i);
    }

    public void updateAll() {
        for (int k = 0; k < cities.size(); k++) {
            updateCity(k);
        }
    }

    public void updateCity(int index) {
        setStatus(index, SingleCityHolder.Status.UPDATING);
    }

    private void setStatus(int index, SingleCityHolder.Status status) {
        cities.get(index).setStatus(status);
    }

}
