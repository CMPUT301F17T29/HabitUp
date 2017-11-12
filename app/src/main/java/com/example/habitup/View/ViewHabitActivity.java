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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.util.ArrayList;

public class ViewHabitActivity extends BaseActivity {

    final private Context context = ViewHabitActivity.this;
    static final int NEW_HABIT = 1;
    static final int VIEW_HABIT = 2;
    static final int EDIT_HABIT = 3;
    public static final String INTENT_HID = "HABIT_INTENT_HID";
    public static final String INTENT_ACTION = "HABIT_INTENT_ACTION";

    // Position of habit in list view
    private int position = -1;

    private ArrayList<Habit> habits;
    private ListView habitListView;
    private ArrayAdapter adapter;
    private ArrayList<HabitEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        // Get bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.habit_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ElasticSearchController.GetUserHabitsTask getUserHabits = new ElasticSearchController.GetUserHabitsTask();
        getUserHabits.execute(String.valueOf(HabitUpApplication.getCurrentUID()));
        try {
            habits = getUserHabits.get();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error retrieving Habits.", Toast.LENGTH_LONG).show();
        }

        // Get user habit events
        ElasticSearchController.GetHabitEventsByUidTask getHabitEvents = new ElasticSearchController.GetHabitEventsByUidTask();
        getHabitEvents.execute(HabitUpApplication.getCurrentUIDAsString());
        try {
            events = getHabitEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEvent - Couldn't get HabitEvents");
        }

        // Initialize habits list view
        habitListView = (ListView) findViewById(R.id.OldHabitLists);

        adapter = new HabitListAdapter(this, R.layout.habit_list_item, habits);
        habitListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        // Handle list view click events
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;

                for (int i = 0; i < habitListView.getChildCount(); i++) {
                    if (i == pos) {
                        highlightItem(view);
                    } else {
                        unhighlightItem(habitListView.getChildAt(i), habits.get(i));
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        habits = HabitUpApplication.getCurrentUser().getHabits().getHabitArrayList();

        ElasticSearchController.GetUserHabitsTask getUserHabits = new ElasticSearchController.GetUserHabitsTask();
        getUserHabits.execute(String.valueOf(HabitUpApplication.getCurrentUID()));
        try {
            habits = getUserHabits.get();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error retrieving Habits.", Toast.LENGTH_LONG).show();
        }

        adapter.notifyDataSetChanged();

        if (habits.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.habits_subheading);
            subHeading.setText("You currently have no habits.");
        }

        // Highlight habits in drawer
        navigationView.setCheckedItem(R.id.habits);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("HabitUpDEBUG", "OnActivityResult in HabitView");

        // Update the Habits List
        // TODO this is not working
        ElasticSearchController.GetUserHabitsTask getUserHabits = new ElasticSearchController.GetUserHabitsTask();
        getUserHabits.execute(String.valueOf(HabitUpApplication.getCurrentUID()));
        try {
            habits = getUserHabits.get();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error retrieving Habits.", Toast.LENGTH_LONG).show();
        }

        adapter.notifyDataSetChanged();

        // If no habits in list, display default message
        if (habits.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.habits_subheading);
            subHeading.setText("You currently have no habits.");
        }

        clearHighlightedRows();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Check if list view item is selected
            if (position < 0) {
                Toast.makeText(context, "Select a habit first.", Toast.LENGTH_SHORT).show();
            } else {
                Habit habit = (Habit) adapter.getItem(position);
                int hid = habit.getHID();

                Log.i("HabitUpDEBUG", "Habit selected - " + habit.getHabitName());

                switch (item.getItemId()) {
                    case R.id.habit_menu_view:
                        goToEditActivity(VIEW_HABIT, hid);
                        return true;
                    case R.id.habit_menu_edit:
                        goToEditActivity(EDIT_HABIT, hid);
                        return true;
                    case R.id.habit_menu_delete:
                        //TODO: ES delete
                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewHabitActivity.this);
                        alert.setTitle("Delete");
                        alert.setMessage("Are you sure you want to delete this habit and its habit events?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (HabitEvent e : events){
                                    if (e.getHID() == habits.get(position).getHID()){
                                            //&& e.getUID() == HabitUpApplication.getCurrentUID()
                                        HabitUpController.deleteHabitEvent(e); // ES delete on associated habit events
                                    }
                                }
                                HabitUpController.deleteHabit(habits.get(position));
                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
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

    private void goToEditActivity(int requestCode, int hid) {
        setResult(RESULT_OK);

        Intent editIntent = new Intent(context, EditHabitActivity.class);
        editIntent.putExtra(INTENT_HID, hid);
        editIntent.putExtra(INTENT_ACTION, requestCode);

        startActivityForResult(editIntent, requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_action_bar:
                Intent addHabitIntent = new Intent(context, AddHabitActivity.class);
                startActivityForResult(addHabitIntent, NEW_HABIT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearHighlightedRows() {
        position = -1;
        for (int i = 0; i < habitListView.getChildCount(); i++) {
            View view = habitListView.getChildAt(i);
            unhighlightItem(view, habits.get(i));
        }
    }

    private void highlightItem(View view) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.teal));
        TextView text = view.findViewById(R.id.habit_name);
        text.setTextColor(getResources().getColor(R.color.white));
    }

    private void unhighlightItem(View view, Habit habit) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        String attribute = habit.getHabitAttribute();
        String color = Attributes.getColour(attribute);

        TextView text = view.findViewById(R.id.habit_name);
        text.setTextColor(Color.parseColor(color));

    }


}


