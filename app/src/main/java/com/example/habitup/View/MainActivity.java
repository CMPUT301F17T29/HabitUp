package com.example.habitup.View;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private ArrayList<Habit> habitList;
    private ListView habitListView;
    private ProfileHabitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize list view for today's habits
        habitListView = (ListView) findViewById(R.id.habit_listview);
        LayoutInflater inflater = this.getLayoutInflater();
        View profileView = (View) inflater.inflate(R.layout.profile_banner, habitListView, false);
        habitListView.addHeaderView(profileView);

        // Set progress bar
        ProgressBar progressBar = (ProgressBar) profileView.findViewById(R.id.progress_bar);
        progressBar.setProgress(50);
        progressBar.setMax(100);

        habitList = new ArrayList<Habit>();

        Habit habit1 = new Habit();
        habit1.setHabitName("Go to the gym");
        habit1.setAttribute("Physical");
        habitList.add(habit1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter = new ProfileHabitsAdapter(this, R.layout.todays_habits, habitList);
        habitListView.setAdapter(adapter);

        if (habitList.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.today_subheading);
            subHeading.setText(getString(R.string.no_habits));
        }
    }
}
