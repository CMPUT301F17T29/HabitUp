package com.example.habitup.View;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This activity displays the user habit statistics, such as progression statuses for
 * all habits.
 */
public class HabitStatistics extends AppCompatActivity {

    ArrayList<Habit> habits;
    ListView habitsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        habits = new ArrayList<>();

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        habitsListView = (ListView) findViewById(R.id.stats_listview);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the user's habits
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        habits.addAll(currentUser.getHabitList().getHabits());

        HabitStatsAdapter adapter = new HabitStatsAdapter(this, R.layout.stat_habit, habits);
        habitsListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
