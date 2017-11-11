package com.example.habitup.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ArrayList<Habit> habitsArrayList;
    private ListView habitListView;
    private ProfileHabitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DEBUG
        UserAccount newUser = new UserAccount("gojoffchoo", "Joff Choo", null);
        try {
            HabitUpApplication.addUserAccount(newUser);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        HabitUpApplication.setCurrentUser(newUser);
        // DEBUG



        // Initialize list view for today's habits
        habitListView = (ListView) findViewById(R.id.habit_listview);
        LayoutInflater inflater = this.getLayoutInflater();
        View profileView = inflater.inflate(R.layout.profile_banner, habitListView, false);
        habitListView.addHeaderView(profileView);

        // Set up the array and adapter
        habitsArrayList = HabitUpController.getTodaysHabits();
        adapter = new ProfileHabitsAdapter(this, R.layout.todays_habits, habitsArrayList);
        habitListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Highlight profile in drawer
        navigationView.setCheckedItem(R.id.profile);

        // Set user's photo
//        findViewById(R.id.drawer_pic).setBackground(); // TODO: PHOTO

        // Set user's display name
        TextView nameField = (TextView) findViewById(R.id.username);
        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        nameField.setText(currentUser.getRealname());

        // Set user's level
        TextView levelField = (TextView) findViewById(R.id.user_level);
        levelField.setText("Level " + String.valueOf(currentUser.getLevel()));

        // Set user's level up in
        TextView levelUpField = (TextView) findViewById(R.id.level_title);
        levelUpField.setText("Level up in " + String.valueOf(currentUser.getXPtoNext() - currentUser.getXP()) + " XP");

        // Set progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setProgress(currentUser.getXP());
        progressBar.setMax(currentUser.getXPtoNext());

        // Set user's Mental value
        TextView attr2Field = (TextView) findViewById(R.id.attribute2_value);
        attr2Field.setText(String.valueOf(currentUser.getAttributes().getValue("Mental")));

        // Set user's Physical value
        TextView attr1Field = (TextView) findViewById(R.id.attribute1_value);
        attr1Field.setText(String.valueOf(currentUser.getAttributes().getValue("Physical")));

        // Set user's Discipline value
        TextView attr3Field = (TextView) findViewById(R.id.attribute3_value);
        attr3Field.setText(String.valueOf(currentUser.getAttributes().getValue("Social")));

        // Set user's Social value
        TextView attr4Field = (TextView) findViewById(R.id.attribute4_value);
        attr4Field.setText(String.valueOf(currentUser.getAttributes().getValue("Discipline")));

        // Retrieve today's habits
        habitsArrayList = HabitUpController.getTodaysHabits();
        adapter.notifyDataSetChanged();

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
}
