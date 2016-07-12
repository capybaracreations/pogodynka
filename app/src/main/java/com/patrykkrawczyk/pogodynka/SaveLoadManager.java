package com.patrykkrawczyk.pogodynka;

import android.content.Context;
import android.util.Log;

import com.patrykkrawczyk.pogodynka.city_data.SingleCityHolder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Patryk Krawczyk on 12.07.2016.
 */
public class SaveLoadManager {

    private static final String FILE_NAME = "pogodynkaCities";

    public static boolean saveCities(Context context, CitiesList cities) {
        boolean status = false;

        // Try with resources requires API 19
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(cities);
            os.close();
            fos.close();
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("patryczek", e.getMessage());
            status = false;
        }

        return status;
    }

    public static CitiesList loadCities(Context context) {
        CitiesList result = null;

        // Try with resources requires API 19
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            result = (CitiesList) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("patryczek", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}
