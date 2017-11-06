package com.example.habitup.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

public class ViewHabitActivity extends BaseActivity {

    private ArrayList<Habit> habits;
    private ListView habitListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        // Get bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.habit_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        habits = new ArrayList<Habit>();

        if (habits.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.habits_subheading);
            subHeading.setText("You currently have no habits.");
        }

        // Initialize habits list view
        habitListView = (ListView) findViewById(R.id.OldHabitLists);

        ArrayAdapter adapter = new HabitListAdapter(this, R.layout.habit_list_item, habits);
        habitListView.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.habit_menu_view:
                    return true;
                case R.id.habit_menu_edit:
                    return true;
                case R.id.habit_menu_delete:
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_action_bar:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


