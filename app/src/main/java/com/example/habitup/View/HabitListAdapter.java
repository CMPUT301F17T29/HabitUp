package com.example.habitup.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This is the adapter for creating the habit list, which displays the habit name, and
 * it's schedule.
 *
 * @see ArrayAdapter
 * @see Habit
 *
 * Created by sharidanbarboza on 2017-10-28.
 */

public class HabitListAdapter extends ArrayAdapter<Habit> {

    // The habits array
    private ArrayList<Habit> habits;

    /**
     * Instantiates a new Habit list adapter.
     * @param context the activity context
     * @param resource the ID of the resource layout
     * @param habits the array list of habits
     */
    public HabitListAdapter(Context context, int resource, ArrayList<Habit> habits) {
        super(context, resource, habits);
        this.habits = habits;
    }

    /**
     * This will either inflate a new view or reuse an existing view.
     * Here the list view row will be initialized and set.
     *
     * @param position the position of the habit in the array
     * @param view the list row view to convert
     * @param viewGroup the parent view
     * @return
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;

        // Inflate a new view
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.habit_list_item, null);
        }

        // Get the habit
        Habit habit = habits.get(position);
        String attributeName = habit.getHabitAttribute();
        String attributeColour = Attributes.getColour(attributeName);

        // Set the name of the habit
        TextView habitNameView = v.findViewById(R.id.habit_name);
        habitNameView.setText(habit.getHabitName());
        habitNameView.setTextColor(Color.parseColor(attributeColour));

        // Get habit schedule
        boolean[] schedule = habit.getHabitSchedule();
        View monView = v.findViewById(R.id.mon_box);
        View tueView = v.findViewById(R.id.tue_box);
        View wedView = v.findViewById(R.id.wed_box);
        View thuView = v.findViewById(R.id.thu_box);
        View friView = v.findViewById(R.id.fri_box);
        View satView = v.findViewById(R.id.sat_box);
        View sunView = v.findViewById(R.id.sun_box);
        View[] textViews = {monView, tueView, wedView, thuView, friView, satView, sunView};

        // Display days of the month for the habit's schedule
        for (int i = 1; i < schedule.length; i++) {
            if (schedule[i]) {
                textViews[i-1].setVisibility(View.VISIBLE);
            } else {
                textViews[i-1].setVisibility(View.GONE);
            }
        }

        return v;
    }
}
