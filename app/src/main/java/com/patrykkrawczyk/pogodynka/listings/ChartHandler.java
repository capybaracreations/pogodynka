package com.patrykkrawczyk.pogodynka.listings;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.ChartEntry;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.style.DashAnimation;
import com.patrykkrawczyk.pogodynka.R;
import com.patrykkrawczyk.pogodynka.city_data.SingleHour;
import com.patrykkrawczyk.pogodynka.listings.DetailsButtonsInterface;
import com.patrykkrawczyk.pogodynka.listings.MyAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk Krawczyk on 10.07.2016.
 */
public class ChartHandler implements DetailsButtonsInterface, OnEntryClickListener {

    private final String[] completeLabels =
            { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };

    private List<SingleHour> hours;
    private static final int CHART_SIZE = 8;
    private LineChartView chartView;
    private LineSet dataset;
    private int currentIndex = 0;
    private Double hoursMin = 0.0;
    private Double hoursMax = 0.0;

    private ChartHandler() {}
    public ChartHandler(LineChartView chartView) {
        this.chartView = chartView;
    }

    public void initializeHours(List<SingleHour> hours) {
        this.hours = hours;
        calculateMinMax();
        setupChart();
    }

    private void calculateMinMax() {
        for(SingleHour hour : hours) {
            if (hour.temperature > hoursMax) hoursMax = hour.temperature;
            if (hour.temperature < hoursMin) hoursMin = hour.temperature;
        }
    }

    private void setupChart() {
        // Tooltip
        chartView.setOnEntryClickListener(this);

        // Data
        dataset = generateLineSet();
        dataset.setColor(chartView.getResources().getColor(R.color.colorPrimary))
                //.setDotsColor(Color.parseColor("#ca9e42"))
                .setThickness(4)
                .setDashed(new float[]{10f, 10f});
        chartView.addData(dataset);

        // Chart
        chartView.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(hoursMin.intValue() - 1, hoursMax.intValue() + 1)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(chartView.getResources().getColor(R.color.colorPrimary))
                .setXAxis(false)
                .setYAxis(false);

        chartView.animateSet(0, new DashAnimation());
        chartView.show();

        applyColors(dataset);
    }

    private void applyColors(LineSet dataset) {
        ArrayList<ChartEntry> entries = dataset.getEntries();

        int cold    = chartView.getResources().getColor(R.color.cold);
        int hot     = chartView.getResources().getColor(R.color.hot);
        int neutral = chartView.getResources().getColor(R.color.neutral);

        for (int k = 0; k < CHART_SIZE; k++) {
            ChartEntry entry = entries.get(k);
            Double temperature = hours.get(k + currentIndex).temperature;
            entry.setColor(MyAdapter.getColorFromTemperature(chartView.getContext(), temperature));
        }
    }

    private LineSet generateLineSet() {
        float[] values = getValuesArray(currentIndex);
        String[] labels = getLabelsArray(currentIndex);

        LineSet result = new LineSet(labels, values);

        return result;
    }

    @Override
    public void onClick(int setIndex, int entryIndex, Rect rect) {
        chartView.dismissAllTooltips();

        SingleHour hour = hours.get(entryIndex + currentIndex);
        Tooltip tooltip  = new Tooltip(chartView.getContext(), R.layout.tooltip, R.id.value);
        ImageView conditions = (ImageView) tooltip.findViewById(R.id.conditionsImageView);
        conditions.setImageDrawable(MyAdapter.getDrawableFromConditions(chartView.getContext(), hour.getConditions()));

        tooltip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        tooltip.setDimensions((int) Tools.fromDpToPx(65), (int) Tools.fromDpToPx(25));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            tooltip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            tooltip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

            tooltip.setPivotX(Tools.fromDpToPx(65) / 2);
            tooltip.setPivotY(Tools.fromDpToPx(50));
        }

        float value = roundTwoDecimals(hour.temperature);

        tooltip.prepare(rect, value);
        chartView.showTooltip(tooltip, true);
    }

    private float roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(d));
    }

    @Override
    public void onClickLeftButton(View view) {
        if (currentIndex > 0) currentIndex--;

        chartView.reset();
        setupChart();
        chartView.dismissAllTooltips();
    }

    @Override
    public void onClickRightButton(View view) {
        if (currentIndex + CHART_SIZE < hours.size()) currentIndex++;

        chartView.reset();
        setupChart();
        chartView.dismissAllTooltips();
    }


    private float[] getValuesArray(int startingIndex) {
        if (startingIndex < 0 || startingIndex+CHART_SIZE > hours.size())
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

    public void clear() {
        chartView.dismissAllTooltips();
        chartView.dismiss();
    }
}
