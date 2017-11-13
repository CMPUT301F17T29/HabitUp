package com.example.habitup.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * Created by sharidanbarboza on 2017-10-28.
 */

public class ProfileHabitsAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public ProfileHabitsAdapter(Context context, int resource, ArrayList<Habit> habits) {
        super(context, resource, habits);
        this.habits = habits;
        this.context = context;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.todays_habits, null);
        }

        final Habit habit = habits.get(position);
        String attributeName = habit.getHabitAttribute();
        String attributeColour = Attributes.getColour(attributeName);

        TextView habitNameView = v.findViewById(R.id.todays_habit_name);
        if (habitNameView != null) {
            habitNameView.setText(habit.getHabitName());
            habitNameView.setTextColor(Color.parseColor(attributeColour));
        }

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

        // Handle click events for check boxes
        final CheckBox checkBox = v.findViewById(R.id.today_habit_checkbox);

        // TODO: Check if habit has an event checked already for that day and check the box
        // TODO: If box is checked, disable check box to be checked
        if (HabitUpController.habitDoneToday(habit)) {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEventIntent = new Intent(context, AddHabitEventActivity.class);
                addEventIntent.putExtra("position", position);
                addEventIntent.putExtra("HABIT_EVENT_HID", habit.getHID());
                addEventIntent.putExtra("profile", 1);
                addEventIntent.putExtra("habit_pos", position);
                addEventIntent.putExtra("habit", habit.getHabitName());
                ((Activity) context).startActivityForResult(addEventIntent, 1);
            }
        });

        return v;
    }
}
