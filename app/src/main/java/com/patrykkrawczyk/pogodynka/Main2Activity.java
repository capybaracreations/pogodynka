package com.patrykkrawczyk.pogodynka;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    private CitiesList cities = new CitiesList();
    private CitiesList citiesnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonsavetest)
    public void OnSaveClick() {
        cities.add(new SingleCityHolder("Szczecin", "10", "11"));
        SaveLoadManager.saveCities(this, cities);
    }

    @OnClick(R.id.buttonloadtest)
    public void OnLoadClick() {
        citiesnew = SaveLoadManager.loadCities(this);

    }

}
