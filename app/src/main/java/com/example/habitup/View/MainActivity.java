package com.example.habitup.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the main activity for the HabitUp application. From this activity, the user
 * can view their profile and the habits scheduled for that day. The user can see what
 * level they are, how much XP they need to level up, and also the value points for each
 * of their Attribute categories.
 * <p>
 * When a user clicks on the checkbox for a specific habit, the user can create a habit
 * event for that day. Once a habit event is made, the user cannot click the checkbox again.
 * <p>
 * The drawer navigation menu can be accessed here.
 *
 * @author Shari Barboza
 */
public class MainActivity extends BaseActivity {

    private ArrayList<Habit> habitsArrayList;
    private RecyclerView habitListView;
    private ProfileHabitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize list view for today's habits
        habitListView = (RecyclerView) findViewById(R.id.habit_listview);
        habitListView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        habitListView.addItemDecoration(itemDecoration);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Highlight profile in drawer
        navigationView.setCheckedItem(R.id.profile);

        // Set up the array and adapter
        habitsArrayList = HabitUpController.getTodaysHabits();
        Collections.sort(habitsArrayList);

        adapter = new ProfileHabitsAdapter(this, habitsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        habitListView.setAdapter(adapter);
        habitListView.setLayoutManager(layoutManager);

        if (habitsArrayList.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.today_subheading);
            subHeading.setText(getString(R.string.no_habits));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent editIntent = new Intent(this, EditProfileActivity.class);
                startActivity(editIntent);
                return true;
            case R.id.stats_profile:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            int habit_pos = data.getExtras().getInt("habit_pos");
            if (resultCode == RESULT_CANCELED && habit_pos >= 0) {
                View view = habitListView.getChildAt(habit_pos);
                CheckBox lastChecked = view.findViewById(R.id.today_habit_checkbox);
                lastChecked.setChecked(false);
                lastChecked.setClickable(false);
            }
    }

}
