package com.example.habitup.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewHabitEventActivity extends BaseActivity {

    static final int NEW_EVENT = 1;

    private ArrayList<HabitEvent> events;
    private ListView eventListView;
    private ArrayAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
 
        // Get bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.event_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Retrieve events from ES for user
        ElasticSearchController.GetHabitEventsByUidTask getHabitEvents = new ElasticSearchController.GetHabitEventsByUidTask();
        getHabitEvents.execute(HabitUpApplication.getCurrentUIDAsString());
        try {
            events = getHabitEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEvent - Couldn't get HabitEvents");
        }

        eventListView = (ListView) findViewById(R.id.event_list);

        // Date format for displaying event date
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMM d, yyyy");

        // Set up list view adapter for habit events
        eventAdapter = new EventListAdapter(this, R.layout.event_list_item, events);
        eventListView.setAdapter(eventAdapter);

        eventAdapter.notifyDataSetChanged();

        // Display if there are no events
        if (events.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.select_event);
            subHeading.setText("You currently have no habit events.");
        }

        // Set up habit type filter spinner
        Spinner habitSpinner = (Spinner) findViewById(R.id.filter_habit_spinner);
        ArrayAdapter<String> habitAdapter = new ArrayAdapter<String>(this, R.layout.habit_spinner);
        habitAdapter.add("All Habit Types");

        // Set up habit types list
        ArrayList<Habit> habitTypes = new ArrayList<Habit>();

        // Populate spinner with habit type names
        for (Habit habit : habitTypes) {
            habitAdapter.add(habit.getHabitName());
        }
        habitSpinner.setAdapter(habitAdapter);
    }

    /**
     * Invoked whenever the activity starts
     */
    @Override
    public void onStart() {
        super.onStart();

        // Highlight events row in drawer
        navigationView.setCheckedItem(R.id.events);
    }

    /**
     * Add the plus button in the top right corner
     * @param menu the add menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    /**
     * Go to the AddHabitEvent activity when the add button is clicked
     * @param item the add button item
     * @return true if add button is clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_action_bar:
                Intent addEventIntent = new Intent(ViewHabitEventActivity.this, AddHabitEventActivity.class);
                startActivityForResult(addEventIntent, NEW_EVENT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Initialize bottom navigation view
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
}
