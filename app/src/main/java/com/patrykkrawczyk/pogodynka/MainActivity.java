package com.patrykkrawczyk.pogodynka;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.RelativeLayout;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dd.processbutton.iml.ActionProcessButton;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.patrykkrawczyk.pogodynka.json.autocomplete.AutoCompleteResult;
import com.patrykkrawczyk.pogodynka.json.autocomplete.RESULT;
import com.patrykkrawczyk.pogodynka.listings.MyAdapter;
import com.patrykkrawczyk.pogodynka.network.WunderGroundAutoComplete;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk Krawczyk on 02.07.2016.
 */
public class MainActivity extends AppCompatActivity implements Callback<AutoCompleteResult>, SweetSheet.OnMenuItemClickListener {



    private final int ANIMATION_SPEED = 500;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.searchButton) ActionProcessButton searchButton;
    @BindView(R.id.searchBox)    MaterialEditText searchBox;

    private Adapter adapter;
    private LayoutManager layoutManager;
    private SweetSheet autoCompleteDialog;

    private CitiesList cities = new CitiesList();
    private ArrayList<MenuEntity> autoCompleteListings = new ArrayList<>();
    private AutoCompleteResult autoCompleteLastResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //noinspection WrongConstant
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        ButterKnife.bind(this);

        initializeAutoCompleteDialog();
        initializeRecyclerView();

        searchButton.setMode(ActionProcessButton.Mode.ENDLESS);
    }

    private void initializeRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(cities);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(ANIMATION_SPEED);
        ScaleInAnimationAdapter scaledAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaledAdapter.setDuration(ANIMATION_SPEED);
        recyclerView.setAdapter(scaledAdapter);

        RecyclerView.ItemAnimator itemAnimator = new LandingAnimator();
        itemAnimator.setAddDuration(ANIMATION_SPEED);
        itemAnimator.setRemoveDuration(ANIMATION_SPEED);
        itemAnimator.setMoveDuration(ANIMATION_SPEED);
        itemAnimator.setChangeDuration(ANIMATION_SPEED);
        recyclerView.setItemAnimator(itemAnimator);

    }

    private void initializeAutoCompleteDialog() {
        autoCompleteDialog = new SweetSheet((RelativeLayout) findViewById(R.id.mainLayout));
        autoCompleteDialog.setMenuList(autoCompleteListings);
        autoCompleteDialog.setDelegate(new RecyclerViewDelegate(true));
        autoCompleteDialog.setBackgroundEffect(new DimEffect(10));
        autoCompleteDialog.setOnMenuItemClickListener(this);
    }

    @OnClick(R.id.searchButton)
    public void onCityNameSearch(View view) {
        String cityName = searchBox.getText().toString();

        if (cityName.isEmpty()) {
            YoYo.with(Techniques.Shake)
                    .duration(1000)
                    .playOn(searchBox);
        } else {
            searchForCityLocation(cityName);
        }
    }

    private void searchForCityLocation(String cityName) {
        searchButton.setProgress(1);
        setSearchUiEnabled(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WunderGroundAutoComplete.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WunderGroundAutoComplete service = retrofit.create(WunderGroundAutoComplete.class);
        Call<AutoCompleteResult> call = service.findCity(cityName);

        call.enqueue(this);
    }

    private void setSearchUiEnabled(boolean enabled) {
        searchButton.setEnabled(enabled);
        searchBox.setEnabled(enabled);
        searchBox.setShowClearButton(enabled);
    }

    private void generateCityListingsAndShowDialog(AutoCompleteResult autoCompleteResult) {
        autoCompleteListings.clear();

        for (RESULT result : autoCompleteResult.RESULTS) {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.iconId = R.drawable.earth;
            menuEntity.title = result.name;
            autoCompleteListings.add(menuEntity);
        }

        autoCompleteDialog.show();
    }

    @Override
    public void onResponse(Call<AutoCompleteResult> call, Response<AutoCompleteResult> response) {
        searchButton.setProgress(100);

        autoCompleteLastResult = response.body();
        if (autoCompleteLastResult.RESULTS.size() > 0) {
            generateCityListingsAndShowDialog(autoCompleteLastResult);
            setSearchUiEnabled(true);
        } else {
            searchButton.setProgress(-1);
            setSearchUiEnabled(true);
        }

    }

    @Override
    public void onFailure(Call<AutoCompleteResult> call, Throwable t) {
        searchButton.setProgress(-1);
        setSearchUiEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (autoCompleteDialog.isShow())
            autoCompleteDialog.dismiss();
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onItemClick(int position, MenuEntity menuEntity) {
        SingleCityHolder singleCityHolder = new SingleCityHolder(autoCompleteLastResult.RESULTS.get(position));
        int newPosition = cities.add(singleCityHolder);

        searchBox.setText("");
        adapter.notifyItemInserted(newPosition);
        //recyclerView.smoothScrollToPosition(newPosition);

        return true;
    }
}
