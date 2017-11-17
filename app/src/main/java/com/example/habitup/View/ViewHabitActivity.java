package com.example.habitup.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
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
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the activity where all the user's habits are displayed alphabetically by habit
 * name. To view a habit's details, the user must click on a habit. To either edit or delete
 * a habit, the user must click and hold on a habit, which will open a context menu. When a user
 * deletes a habit, all habit events associated with that habit will be delete as well.
 * <p>
 * The drawer navigation menu can be accessed here.
 *
 * @author Shari Barboza
 */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.i("HabitUpDEBUG", "ViewHabitActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        // Initialize habits list view
        habitListView = (ListView) findViewById(R.id.OldHabitLists);

        // Handle list view click events
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;

                final Habit habit = (Habit) adapter.getItem(pos);
                int hid = habit.getHID();
                String habitName = habit.getHabitName();
                goToEditActivity(VIEW_HABIT, hid, habitName);

            }
        });

        registerForContextMenu(habitListView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        habits = currentUser.getHabitList().getHabits();

        // Sort 'em.
        Collections.sort(habits);

        habitListView = (ListView) findViewById(R.id.OldHabitLists);

        adapter = new HabitListAdapter(this, R.layout.habit_list_item, habits);
        habitListView.setAdapter(adapter);

        if (habits.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.habits_subheading);
            subHeading.setText("You currently have no habits.");
        }

        // Highlight habits in drawer
        navigationView.setCheckedItem(R.id.habits);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select action");
        menu.add(0, 1, 1, "Edit");
        menu.add(0, 2, 2, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        final Habit habit = (Habit) adapter.getItem(position);
        int hid = habit.getHID();
        String habitName = habit.getHabitName();

        switch (item.getItemId()) {
            case 1:
                goToEditActivity(EDIT_HABIT, hid, habitName);
                return true;
            case 2:
                deleteHabit(habit);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void goToEditActivity(int requestCode, int hid, String name) {
        setResult(RESULT_OK);

        Intent editIntent = new Intent(context, EditHabitActivity.class);
        editIntent.putExtra(INTENT_HID, hid);
        editIntent.putExtra(INTENT_ACTION, requestCode);
        editIntent.putExtra("HABIT NAME", name);
        startActivity(editIntent);
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

    private void deleteHabit(final Habit habit) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewHabitActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete this Habit?  This will also delete any events completed that were associated with the Habit.");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HabitUpController.deleteHabitEventsForHabit(habit);
                HabitUpController.deleteHabit(habit);
                adapter.remove(habit);
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
    }


}


