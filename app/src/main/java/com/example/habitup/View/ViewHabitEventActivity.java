package com.example.habitup.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewHabitEventActivity extends BaseActivity {

    private final Context context = ViewHabitEventActivity.this;
    public static final int NEW_EVENT = 1;
    public static final int VIEW_EVENT = 2;
    public static final int EDIT_EVENT = 3;
    public static final String HABIT_EVENT_UID = "HABIT_EVENT_UID";
    public static final String HABIT_EVENT_HID = "HABIT_EVENT_HID";
    public static final String HABIT_EVENT_EID = "HABIT_EVENT_EID";
    public static final String HABIT_EVENT_ACTION = "HABIT_EVENT_ACTION";

    // Position of event in list view
    private int position = -1;

    private ArrayList<HabitEvent> events;
    private ListView eventListView;
    private EventListAdapter eventAdapter;
    private ArrayList<HabitEvent> filtList;
    private EditText commentFilter;

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

        commentFilter = (EditText) findViewById(R.id.filter_comment);

        eventListView = (ListView) findViewById(R.id.event_list);

        // Date format for displaying event date
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMM d, yyyy");

        //sort by completedate
        Collections.sort(events, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent e1, HabitEvent e2) {
                return e1.getCompletedate().compareTo(e2.getCompletedate());
            }
        });
        Collections.reverse(events);


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

        // comment filter through list
        commentFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = commentFilter.getText().toString().toLowerCase(Locale.getDefault());
                filtList = new ArrayList<HabitEvent>();
                filtList.clear();
                if (text.length()==0){
                    filtList.addAll(events);
                }
                else {
                    for (HabitEvent e : events) {
                       if (e.getComment().toLowerCase(Locale.getDefault()).contains(text)) {
                           filtList.add(e);
                       }
                    }
                }
                eventAdapter = new EventListAdapter(context, R.layout.event_list_item, filtList);
                eventListView.setAdapter(eventAdapter);

                eventAdapter.notifyDataSetChanged();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clearHighlightedRows();

        // Retrieve events from ES for user
        ElasticSearchController.GetHabitEventsByUidTask getHabitEvents = new ElasticSearchController.GetHabitEventsByUidTask();
        getHabitEvents.execute(HabitUpApplication.getCurrentUIDAsString());
        try {
            events = getHabitEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEvent - Couldn't get HabitEvents");
        }

        // Set up list view adapter for habit events
        eventAdapter = new EventListAdapter(this, R.layout.event_list_item, events);
        eventListView.setAdapter(eventAdapter);

        eventAdapter.notifyDataSetChanged();

        // Display if there are no events
        if (events.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.select_event);
            subHeading.setText("You currently have no habit events.");
        }

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
                        //ES deletes through eid
                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewHabitEventActivity.this);
                        alert.setTitle("Delete");
                        alert.setMessage("Are you sure you want to delete this habit event?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HabitUpController.deleteHabitEvent(eventAdapter.getItem(position)); // ES delete
                                eventAdapter.remove(eventAdapter.getItem(position)); // app view delete
                                eventListView.setAdapter(eventAdapter);
                                eventAdapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alert.show();
                        return true;
                }
            }
            return false;
        }

    };

    private void goToEditActivity(int requestCode) {
        setResult(RESULT_OK);
        Intent editIntent = new Intent(context, EditHabitEventActivity.class);
        int uid = HabitUpApplication.getCurrentUID();
        int hid = ((HabitEvent) eventAdapter.getItem(position)).getHID();
        String eid = ((HabitEvent) eventAdapter.getItem(position)).getEID();

        Log.i("HabitUpDEBUG", "ViewHabitEvent eid " + eid);

        editIntent.putExtra(HABIT_EVENT_UID, uid);
        editIntent.putExtra(HABIT_EVENT_HID, hid);
        editIntent.putExtra(HABIT_EVENT_EID, eid);
        editIntent.putExtra(HABIT_EVENT_ACTION, requestCode);
        startActivityForResult(editIntent, requestCode);
    }

    private void clearHighlightedRows() {
        position = -1;
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
