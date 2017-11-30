package com.example.habitup.View;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by barboza on 2017-11-29.
 */

public class ChartFragment extends Fragment {

    private int page;

    FragmentPagerAdapter adapterViewPager;

    ArrayList<String> dates;
    DateTimeFormatter formatter;
    LocalDate minimumDate;

    BarChart chart;
    List<BarEntry> entries;
    BarDataSet set;

    HashMap<String, Integer> dateMap;

    // newInstance constructor for creating fragment with arguments
    public static ChartFragment newInstance(int page) {
        ChartFragment fragmentFirst = new ChartFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_frag, container, false);

        // Set up timeline chart
        chart = view.findViewById(R.id.timeline);
        entries = new ArrayList<>();

        // Set up the x-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        formatter = DateTimeFormatter.ofPattern("MMM d");
        dates = new ArrayList<>();

        int numDays = 14;
        LocalDate today = LocalDate.now();
        minimumDate = today.minusDays(numDays);
        LocalDate currentDay = minimumDate;

        dateMap = new HashMap<>();

        for (int i = 0; i < numDays + 1; i++) {
            float index = (float) i;

            String dateString = currentDay.format(formatter);
            dates.add(dateString);
            dateMap.put(dateString, i);

            BarEntry barEntry = new BarEntry(index, new float[] { 0, 0, 0, 0 });
            entries.add(barEntry);

            currentDay = currentDay.plusDays(1);
        }

        xAxis.setValueFormatter(new MyXAxisValueFormatter(dates));
        xAxis.setLabelCount(numDays);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45f);

        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        // Get user events
        ArrayList<HabitEvent> eventList = currentUser.getEventList().getEvents();

        for (HabitEvent event : eventList) {
            if (withinDateRange(event.getCompletedate(), minimumDate)) {
                addChartEntry(event);
            }
        }

        set = new BarDataSet(entries, "");
        int red = Color.parseColor(Attributes.getColour("Physical"));
        int purple = Color.parseColor(Attributes.getColour("Mental"));
        int green = Color.parseColor(Attributes.getColour("Discipline"));
        int blue = Color.parseColor(Attributes.getColour("Social"));
        int[] colors = {red, purple, green, blue};
        set.setColors(colors);
        set.setStackLabels(new String[]{"Physical", "Mental", "Discipline", "Social"});
        set.setDrawValues(false);

        BarData data = new BarData(set);
        data.setBarWidth(0.6f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);

        chart.setData(data);
        chart.setFitBars(true);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getDescription().setEnabled(false);
        animateChart();
        chart.invalidate();

        return view;
    }

    public void animateChart() {
        if (chart != null) {
            chart.animateY(1000);
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> values;

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values.get((int) value);
        }
    }

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,###"); // use no decimals
        }

        @Override
        public String getFormattedValue(float value, AxisBase yAxis) {
            return mFormat.format(value);
        }
    }

    private void addChartEntry(HabitEvent event) {
        String date = event.getCompletedate().format(formatter);
        int index = dateMap.get(date);
        BarEntry entry = entries.get(index);

        float[] stacks = entry.getYVals();

        String attribute = event.getHabitAttribute();
        if (attribute.equals("Physical")) {
            stacks[0] += 1;
        } else if (attribute.equals("Mental")) {
            stacks[1] += 1;
        } else if (attribute.equals("Discipline")) {
            stacks[2] += 1;
        } else if (attribute.equals("Social")) {
            stacks[3] += 1;
        }
        entry.setVals(stacks);
    }

    private Boolean withinDateRange(LocalDate date, LocalDate minimumDate) {
        return date.isAfter(minimumDate) || date.equals(minimumDate);
    }

}
