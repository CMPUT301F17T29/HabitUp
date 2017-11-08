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
 * Created by sharidanbarboza on 2017-10-28.
 */

public class HabitListAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;

    public HabitListAdapter(Context context, int resource, ArrayList<Habit> habits) {
        super(context, resource, habits);
        this.habits = habits;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.habit_list_item, null);
        }

        Habit habit = habits.get(position);
        String attributeName = habit.getHabitAttribute();
        String attributeColour = Attributes.getColour(attributeName);

        if (habit != null) {
            TextView habitNameView = v.findViewById(R.id.todays_habit_name);

            if (habitNameView != null) {
                habitNameView.setText(habit.getHabitName());
                habitNameView.setTextColor(Color.parseColor(attributeColour));
            }
        }

        return v;
    }
}
