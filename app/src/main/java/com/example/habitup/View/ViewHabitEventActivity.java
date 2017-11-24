package com.example.habitup.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * This is the activity where the user can see their habit event history, which displays
 * a list of all the habit events completed, sorted by most recent date. To view an event's
 * details, the user must click on an event. To either edit or delete an event, the user must
 * click and hold on an event, which will open a context menu.
 * <p>
 * The user can filter the events either by comment text, habit type, or both. Once text is
 * entered in the comment field or a habit is selected from the dropdown menu, the matching
 * events will immediately be displayed.
 * <p>
 * The drawer navigation menu can be accessed here.
 *
 * @author Shari Barboza
 */
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
    private RecyclerView eventListView;
    private EventListAdapter eventAdapter;
    private EditText commentFilter;
    private ArrayList<Habit> habitTypes;
    private Spinner habitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.i("HabitUpDEBUG", "ViewHabitEventActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        events = new ArrayList<>();
        eventListView = (RecyclerView) findViewById(R.id.event_list);
        eventListView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        eventListView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        eventListView.addItemDecoration(itemDecoration);
    }

    /**
     * Invoked whenever the activity starts
     */
    @Override
    public void onStart() {
        super.onStart();

        // Retrieve events
        events.clear();
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        events.addAll(currentUser.getEventList().getEvents());

        eventAdapter = new EventListAdapter(this, events);
        eventListView.setAdapter(eventAdapter);

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

        // Sort by completedate
        Collections.sort(events);

        // Display if there are no events
        if (events.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.select_event);
            subHeading.setText("You currently have no habit events.");
        }

        habitTypes = currentUser.getHabitList().getHabits();

        // Set up habit type filter spinner
        habitSpinner = (Spinner) findViewById(R.id.filter_habit_spinner);
        final ArrayAdapter<String> habitAdapter = new ArrayAdapter<>(this, R.layout.habit_spinner);
        habitAdapter.add("All Habit Types");
        // Set up habit types list
        habitTypes = currentUser.getHabitList().getHabits();

        // Populate spinner with habit type names
        for (Habit habit : habitTypes) {
            habitAdapter.add(habit.getHabitName());
        }
        habitSpinner.setAdapter(habitAdapter);

        // Spinner select
        habitSpinner.setOnItemSelectedListener(spinnerListener);

        commentFilter = (EditText) findViewById(R.id.filter_comment);
        commentFilter.setOnKeyListener(cFilter);

        // Highlight events row in drawer
        navigationView.setCheckedItem(R.id.events);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_action_bar:

                if (habitTypes.size() == 0) {
                    Log.i("HabitUpDEBUG", "ViewHabitEvents/Can't create HabitEvent with no Habits");
                    Toast.makeText(getApplicationContext(), "Error: You cannot add Habit Events without any Habits.", Toast.LENGTH_LONG).show();
                    return false;
                }

                Intent addEventIntent = new Intent(ViewHabitEventActivity.this, AddHabitEventActivity.class);
                addEventIntent.putExtra("profile", 0);
                startActivityForResult(addEventIntent, NEW_EVENT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToEditActivity(int requestCode) {
        setResult(RESULT_OK);
        Intent editIntent = new Intent(context, EditHabitEventActivity.class);
        int uid = HabitUpApplication.getCurrentUID();
        int hid = eventAdapter.getItem(position).getHID();
        String eid = eventAdapter.getItem(position).getEID();

//        Log.i("HabitUpDEBUG", "ViewHabitEvent eid " + eid);

        editIntent.putExtra(HABIT_EVENT_UID, uid);
        editIntent.putExtra(HABIT_EVENT_HID, hid);
        editIntent.putExtra(HABIT_EVENT_EID, eid);
        editIntent.putExtra(HABIT_EVENT_ACTION, requestCode);
        editIntent.putExtra("profile", 0);
        editIntent.putExtra("EVENT POSITION", position);
        startActivity(editIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int levelledUp = data.getExtras().getInt("levelled_up");

        if (levelledUp == 1) {
            displayLevelUp();
        }
    }

    private void deleteEvent() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewHabitEventActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete this habit event?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HabitUpController.deleteHabitEvent(eventAdapter.getItem(position)); // ES delete
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

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

            // Reset comment search when filtering by Habit
            commentFilter.setText("");

            ArrayList<HabitEvent> filteredList;
            UserAccount currentUser = HabitUpApplication.getCurrentUser();
            // pos 0 means All Habits: reset to full habit history view
            if (pos == 0) {
                filteredList = currentUser.getEventList().getEvents();

                // Otherwise, a Habit type was selected by which to filter
            } else {
                Habit habitType = habitTypes.get(pos-1);
                filteredList = currentUser.getEventList().getEventsFromHabit(habitType.getHID());
            }

            events.clear();
            events.addAll(filteredList);
            Collections.sort(events);
            eventAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    EditText.OnKeyListener cFilter = new EditText.OnKeyListener() {

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

            habitSpinner.setOnItemSelectedListener(null);
            habitSpinner.setSelection(0, false);
            habitSpinner.setOnItemSelectedListener(spinnerListener);

            if ((keyEvent.getAction() == KeyEvent.ACTION_UP) && (i == KeyEvent.KEYCODE_ENTER)) {

                InputMethodManager methodMan = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                methodMan.toggleSoftInput(0, 0);

                ArrayList<HabitEvent> commentMatches = null;
                String searchText = commentFilter.getText().toString().trim().toLowerCase(Locale.getDefault());

                if (searchText.isEmpty()) {
                    Log.i("HabitUpDEBUG", "ViewHabitEvents/FilterENTER - search text empty");
                    Toast.makeText(getApplicationContext(), "Error: Please enter a string to search comments", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    ElasticSearchController.GetHabitEventsForCommentMatch commentSearch = new ElasticSearchController.GetHabitEventsForCommentMatch();
                    commentSearch.execute(searchText);
                    try {
                        commentMatches = commentSearch.get();
                    } catch (Exception e) {
                        Log.i("HabitUpDEBUG", "ViewHabitEvents/CommentSearch - failed to get matching HabitEvents");
                    }
                }

                events.clear();
                events.addAll(commentMatches);
                Collections.sort(events);
                eventAdapter.notifyDataSetChanged();
            }
            return true;
        }
    };

    private void displayLevelUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewHabitEventActivity.this);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.levelup_dialog, null);
        builder.setView(v);

        TextView newLevel = v.findViewById(R.id.new_level);

        // Get user's new level
        int level = HabitUpApplication.getCurrentUser().getLevel();
        newLevel.setText("Level " + level);

        Button closeButton = v.findViewById(R.id.close_level_dialog);

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Close dialog
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

}

