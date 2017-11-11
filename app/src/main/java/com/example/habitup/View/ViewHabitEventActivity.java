package com.example.habitup.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewHabitEventActivity extends BaseActivity {

    final private Context context = ViewHabitEventActivity.this;
    static final int NEW_EVENT = 1;
    static final int VIEW_EVENT = 2;
    static final int EDIT_EVENT = 3;

    // Position of event in list view
    private int position = -1;

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

        // Handle list view click events
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;

                for (int i = 0; i < eventListView.getChildCount(); i++) {
                    if (i == pos) {
                        highlightItem(view);
                    } else {
                        unhighlightItem(eventListView.getChildAt(i), events.get(i));
                    }
                }
            }
        });
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

            // Check if list view item is selected
            if (position < 0) {
                Toast.makeText(context, "Select an event first.", Toast.LENGTH_SHORT).show();
            } else {
                switch (item.getItemId()) {
                    case R.id.habit_menu_view:
                        goToEditActivity(VIEW_EVENT);
                        return true;
                    case R.id.habit_menu_edit:
                        goToEditActivity(EDIT_EVENT);
                        return true;
                    case R.id.habit_menu_delete:
                        return true;
                }
            }
            return false;
        }

    };

    private void goToEditActivity(int requestCode) {
        setResult(RESULT_OK);
        Intent editIntent = new Intent(context, EditHabitEventActivity.class);
        editIntent.putExtra("position", position);
        editIntent.putExtra("action", requestCode);
        startActivityForResult(editIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clearHighlightedRows();
    }

    private void clearHighlightedRows() {
        for (int i = 0; i < eventListView.getChildCount(); i++) {
            View view = eventListView.getChildAt(i);
            unhighlightItem(view, events.get(i));
        }
    }

    private void highlightItem(View view) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.teal));

        int whiteColor = getResources().getColor(R.color.white);
        TextView text = view.findViewById(R.id.event_name);
        text.setTextColor(whiteColor);

        TextView comment = view.findViewById(R.id.event_comment);
        comment.setTextColor(whiteColor);

        TextView dateText = view.findViewById(R.id.event_date);
        dateText.setTextColor(whiteColor);
    }

    private void unhighlightItem(View view, HabitEvent event) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

        Habit eventHabit;
        ElasticSearchController.GetHabitsTask getHabit = new ElasticSearchController.GetHabitsTask();
        getHabit.execute(String.valueOf(event.getHID()));
        try {
            eventHabit = getHabit.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEventActivity - couldn't get Habit");
            eventHabit = new Habit(-1);
            eventHabit.setHabitName("ERROR");
        }

        String attribute = eventHabit.getHabitAttribute();
        String color = Attributes.getColour(attribute);

        int lightGray = getResources().getColor(R.color.lightgray);
        TextView text = view.findViewById(R.id.event_name);
        text.setTextColor(Color.parseColor(color));

        TextView comment = view.findViewById(R.id.event_comment);
        comment.setTextColor(lightGray);

        TextView dateText = view.findViewById(R.id.event_date);
        dateText.setTextColor(lightGray);

    }
}
