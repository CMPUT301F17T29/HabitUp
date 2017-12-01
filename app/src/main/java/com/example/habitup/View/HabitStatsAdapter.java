package com.example.habitup.View;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * Created by barboza on 11/27/17.
 */

public class HabitStatsAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;

    public HabitStatsAdapter(Context context, int resource, ArrayList<Habit> habits) {
        super(context, resource, habits);
        this.habits = habits;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            // Get view from the XML file
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.stat_habit, null);
        }

        Habit habit = this.habits.get(position);
        habit.updateHabitsPossible();

        TextView nameView = v.findViewById(R.id.stats_habit_name);
        nameView.setText(habit.getHabitName());
        nameView.setTextColor(Color.parseColor(Attributes.getColour(habit.getHabitAttribute())));

        TextView statusView = v.findViewById(R.id.stats_habit_status);
        statusView.setText(habit.getHabitsDone() + "/" + habit.getHabitsPossible());

        ProgressBar progressBar = v.findViewById(R.id.stats_habit_progress);
        progressBar.setProgress(habit.getPercent());

        TextView extraView = v.findViewById(R.id.stats_extra);
        extraView.setText(String.valueOf(habit.getHabitsDoneExtra()));

        return v;
    }

}
