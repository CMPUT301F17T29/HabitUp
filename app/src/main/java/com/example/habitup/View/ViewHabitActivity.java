package com.example.habitup.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

public class ViewHabitActivity extends BaseActivity {

    final private Context context = ViewHabitActivity.this;
    static final int NEW_HABIT = 1;
    static final int VIEW_HABIT = 2;
    static final int EDIT_HABIT = 3;

    // Position of habit in list view
    private int position = -1;

    private ArrayList<Habit> habits;
    private ListView habitListView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        // Get bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.habit_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        habits = HabitUpController.getCurrentUser().getHabits().getHabitArrayList();

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
                        unhighlightItem(habitListView.getChildAt(i));
                    }
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Check if list view item is selected
            if (position < 0) {
                Toast.makeText(context, "Select a habit first.", Toast.LENGTH_SHORT).show();
            } else {
                switch (item.getItemId()) {
                    case R.id.habit_menu_view:
                        goToEditActivity(VIEW_HABIT);
                        return true;
                    case R.id.habit_menu_edit:
                        goToEditActivity(EDIT_HABIT);
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
        Intent editIntent = new Intent(context, EditHabitActivity.class);
        editIntent.putExtra("position", position);
        editIntent.putExtra("action", requestCode);
        startActivityForResult(editIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get position and unhighlight selected list item
        if (requestCode == EDIT_HABIT) {
            position = data.getExtras().getInt("position");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        habits = HabitUpController.getCurrentUser().getHabits().getHabitArrayList();
        adapter.notifyDataSetChanged();

        if (habits.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.habits_subheading);
            subHeading.setText("You currently have no habits.");
        }

        // Highlight habits in drawer
        navigationView.setCheckedItem(R.id.habits);
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

    private void highlightItem(View view) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.whitegray));
    }

    private void unhighlightItem(View view) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
    }


}


