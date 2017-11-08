package com.example.habitup.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private ArrayList<Habit> habitsArrayList;
    private ListView habitListView;
    private ProfileHabitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DEBUG
        HabitUpController hupCtl = new HabitUpController();
        // DEBUG

        // Initialize list view for today's habits
        habitListView = (ListView) findViewById(R.id.habit_listview);
        LayoutInflater inflater = this.getLayoutInflater();
        View profileView = inflater.inflate(R.layout.profile_banner, habitListView, false);
        habitListView.addHeaderView(profileView);

        // TODO: Retrieve habits from HabitList model
        habitsArrayList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Highlight profile in drawer
        navigationView.setCheckedItem(R.id.profile);

        adapter = new ProfileHabitsAdapter(this, R.layout.todays_habits, habitsArrayList);
        habitListView.setAdapter(adapter);

        // Set user's photo
//        findViewById(R.id.drawer_pic).setBackground(); // TODO: PHOTO

        // Set user's display name
        TextView nameField = (TextView) findViewById(R.id.username);
        nameField.setText(HabitUpController.getCurrentUser().getRealname());

        // Set user's level
        TextView levelField = (TextView) findViewById(R.id.user_level);
        levelField.setText("Level " + String.valueOf(HabitUpController.getCurrentUser().getLevel()));

        // Set user's level up in
        TextView levelUpField = (TextView) findViewById(R.id.level_title);
        levelUpField.setText("Level up in " + String.valueOf(HabitUpController.getCurrentUser().getXPtoNext()) + " XP");

        // Set progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setProgress(HabitUpController.getCurrentUser().getXP());
        progressBar.setMax(HabitUpController.getCurrentUser().getXPtoNext());

        // Set user's Mental value
        TextView attr2Field = (TextView) findViewById(R.id.attribute2_value);
        attr2Field.setText(String.valueOf(HabitUpController.getCurrentUser().getAttributes().getValue("Mental")));

        // Set user's Physical value
        TextView attr1Field = (TextView) findViewById(R.id.attribute1_value);
        attr1Field.setText(String.valueOf(HabitUpController.getCurrentUser().getAttributes().getValue("Physical")));

        // Set user's Discipline value
        TextView attr3Field = (TextView) findViewById(R.id.attribute3_value);
        attr3Field.setText(String.valueOf(HabitUpController.getCurrentUser().getAttributes().getValue("Social")));

        // Set user's Social value
        TextView attr4Field = (TextView) findViewById(R.id.attribute4_value);
        attr4Field.setText(String.valueOf(HabitUpController.getCurrentUser().getAttributes().getValue("Discipline")));



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
}
