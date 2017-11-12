package com.example.habitup.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private int adapterSize;

    private ArrayList<HabitEvent> events;
    private RecyclerView eventListView;
    private EventListAdapter eventAdapter;
    private EditText commentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("HabitUpDEBUG", "ViewHabitEventActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        eventListView = (RecyclerView) findViewById(R.id.event_list);
        eventListView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        eventListView.addItemDecoration(itemDecoration);

    }

    /**
     * Invoked whenever the activity starts
     */
    @Override
    public void onStart() {
        super.onStart();

        // Retrieve events from ES for user
        ElasticSearchController.GetHabitEventsByUidTask getHabitEvents = new ElasticSearchController.GetHabitEventsByUidTask();
        getHabitEvents.execute(HabitUpApplication.getCurrentUIDAsString());
        try {
            events = getHabitEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEvent - Couldn't get HabitEvents");
        }

        eventListView = (RecyclerView) findViewById(R.id.event_list);
        eventListView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        eventListView.addItemDecoration(itemDecoration);

        eventAdapter = new EventListAdapter(this, events);
        adapterSize = eventAdapter.getItemCount();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        eventListView.setAdapter(eventAdapter);
        eventListView.setLayoutManager(layoutManager);

        eventAdapter.notifyDataSetChanged();

        eventAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                eventAdapter.setPosition(eventListView.getChildAdapterPosition(v));
                return false;
            }
        });

        eventAdapter.setOnItemClickListener(new EventListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                position = pos;
                goToEditActivity(VIEW_EVENT);
            }
        });

        commentFilter = (EditText) findViewById(R.id.filter_comment);

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
        ElasticSearchController.GetUserHabitsTask userHabits = new ElasticSearchController.GetUserHabitsTask();
        userHabits.execute(HabitUpApplication.getCurrentUIDAsString());
        ArrayList<Habit> habitTypes;

        try {
            habitTypes = userHabits.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEvent, couldn't get HabitTypes for user");
            habitTypes = null;
        }

        // Populate spinner with habit type names
        for (Habit habit : habitTypes) {
            habitAdapter.add(habit.getHabitName());
        }
        habitSpinner.setAdapter(habitAdapter);

        /*
        // comment filter through list
        commentFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ViewHabitEventActivity.this.eventAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        */

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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        position = eventAdapter.getPosition();

        switch (item.getItemId()) {
            case 1:
                goToEditActivity(EDIT_EVENT);
                return true;
            case 2:
                deleteEvent();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        startActivity(editIntent);
    }

    private void deleteEvent() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewHabitEventActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete this habit event?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HabitUpController.deleteHabitEvent(events.get(position)); // ES delete
                eventAdapter.removeItem(position); // app view delete
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
    }

}
