package com.example.habitup.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * Created by barboza on 2017-11-29.
 */

public class HabitStatusFragment extends Fragment {

    private int page;

    ArrayList<Habit> habits;
    ListView habitsListView;

    // newInstance constructor for creating fragment with arguments
    public static HabitStatusFragment newInstance(int page) {
        HabitStatusFragment fragmentFirst = new HabitStatusFragment();
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
        habits = new ArrayList<>();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_frag, container, false);
        habitsListView = view.findViewById(R.id.stats_listview);

        // Get the user's habits
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        habits.addAll(currentUser.getHabitList().getHabits());

        HabitStatsAdapter adapter = new HabitStatsAdapter(getContext(), R.layout.stat_habit, habits);
        habitsListView.setAdapter(adapter);

        return view;
    }
}
