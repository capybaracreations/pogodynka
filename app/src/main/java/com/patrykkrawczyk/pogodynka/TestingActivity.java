package com.patrykkrawczyk.pogodynka;

import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.db.chart.Tools;
import com.db.chart.model.ChartEntry;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.style.DashAnimation;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour;
import com.patrykkrawczyk.pogodynka.listings.MyAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestingActivity extends AppCompatActivity {

    private static final int CHART_SIZE = 8;
    private ImageView refreshButton;
    private ImageView deleteButton;
    private LineChartView chartView;
    private final String[] completeLabels = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private List<SingleHour> hours = new ArrayList<>();
    private float[] completeValues = {3.5f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f, 3.14f, 3.5f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f, 3.14f, 7f, 8.3f, 7.0f, 3.14f};
    private Tooltip chartTooltip;
    private LineSet dataset;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        chartView = (LineChartView) findViewById(R.id.chartView);
        refreshButton = (ImageView) findViewById(R.id.minusButton);
        deleteButton = (ImageView) findViewById(R.id.plusButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) currentIndex--;

                chartView.reset();
                setupChart();
                chartView.dismissAllTooltips();
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex + CHART_SIZE < completeValues.length) currentIndex++;

                chartView.reset();
                setupChart();
                chartView.dismissAllTooltips();
            }
        });

        for (int k =0; k < 24 ; k ++)
        {
            hours.add(new SingleHour(""+k, (double)completeValues[k]));
        }

        setupChart();
    }

    private float[] getValuesArray(int startingIndex) {
        if (startingIndex < 0 || startingIndex+CHART_SIZE > completeValues.length)
            throw new IndexOutOfBoundsException("Requested chart values for bad index");

        float[] result = new float[CHART_SIZE];

        for (int k = 0; k < result.length; k++)
            result[k] = (float) ((double)hours.get(k + startingIndex).temperature);

        return result;
    }

    private String[] getLabelsArray(int startingIndex) {
        if (startingIndex < 0 || startingIndex+CHART_SIZE > completeLabels.length)
            throw new IndexOutOfBoundsException("Requested chart labels for bad index");

        String[] result = new String[CHART_SIZE];

        for (int k = 0; k < result.length; k++)
            result[k] = completeLabels[k + startingIndex];

        return result;
    }


    private void setupChart() {
        chartTooltip = new Tooltip(this, R.layout.linechart_three_tooltip, R.id.value);

        chartTooltip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        chartTooltip.setDimensions((int) Tools.fromDpToPx(65), (int) Tools.fromDpToPx(25));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            chartTooltip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            chartTooltip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

            chartTooltip.setPivotX(Tools.fromDpToPx(65) / 2);
            chartTooltip.setPivotY(Tools.fromDpToPx(25));
        }

        chartView.setTooltips(chartTooltip);


        // Data
        dataset = generateLineSet();
        dataset.setColor(getResources().getColor(R.color.colorPrimary))
                //.setDotsColor(Color.parseColor("#ca9e42"))
                .setThickness(4)
                .setDashed(new float[]{10f, 10f});
        chartView.addData(dataset);


        // Chart
        chartView.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(0, 20)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(getResources().getColor(R.color.colorPrimary))
                .setXAxis(false)
                .setYAxis(false);

        chartView.animateSet(0, new DashAnimation());
        chartView.show();

        applyColors(dataset);
    }

    private void applyColors(LineSet dataset) {
        ArrayList<ChartEntry> entries = dataset.getEntries();

        int cold    = getResources().getColor(R.color.cold);
        int hot     = getResources().getColor(R.color.hot);
        int neutral = getResources().getColor(R.color.neutral);

        for (int k = 0; k < CHART_SIZE; k++) {
            ChartEntry entry = entries.get(k);
            Double temperature = hours.get(k + currentIndex).temperature;
            entry.setColor(MyAdapter.getColorFromTemperature(this, temperature));
        }
    }

    private LineSet generateLineSet() {
        float[] values = getValuesArray(currentIndex);
        String[] labels = getLabelsArray(currentIndex);

        LineSet result = new LineSet(labels, values);

        return result;
    }


}
