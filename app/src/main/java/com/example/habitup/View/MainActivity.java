package com.example.habitup.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    ProgressBar progressBar;

    private ArrayList<Habit> habitList;
    private ListView habitListView;
    private ProfileHabitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();

        // Initialize list view for today's habits
        habitListView = (ListView) findViewById(R.id.habit_listview);
        LayoutInflater inflater = this.getLayoutInflater();
        View profileView = (View) inflater.inflate(R.layout.profile_banner, habitListView, false);
        habitListView.addHeaderView(profileView);

        // Set progress bar
        progressBar = (ProgressBar) profileView.findViewById(R.id.progress_bar);
        progressBar.setProgress(50);
        progressBar.setMax(100);

        habitList = new ArrayList<Habit>();

        // Create dummy habit objects
        Habit habit1 = new Habit();
        habit1.setHabitName("Go to the gym");
        habit1.setAttribute("Physical");

        Habit habit2 = new Habit();
        habit2.setHabitName("Study for CMPUT 301");
        habit2.setAttribute("Mental");

        Habit habit3 = new Habit();
        habit3.setHabitName("Practice piano");
        habit3.setAttribute("Discipline");

        Habit habit4 = new Habit();
        habit4.setHabitName("Attend CMPUT 469");
        habit4.setAttribute("Social");

        Habit habit5 = new Habit();
        habit5.setHabitName("Clean room");
        habit5.setAttribute("Discipline");

        habitList.add(habit1);
        habitList.add(habit2);
        habitList.add(habit3);
        habitList.add(habit4);
        habitList.add(habit5);

        // Change transparency of profile image
        ImageView profileImage = (ImageView) findViewById(R.id.drawer_pic);
        profileImage.setAlpha(1f);

    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter = new ProfileHabitsAdapter(this, R.layout.todays_habits, habitList);
        habitListView.setAdapter(adapter);

        if (habitList.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.today_subheading);
            subHeading.setText(getString(R.string.no_habits));
        }
    }

    public void initNavigationDrawer() {
        // Taken from https://www.learn2crack.com/2016/03/android-material-design-sliding-navigation-drawer.html
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.profile:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.habits:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.events:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.friends:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.requests:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.finds:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.map:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
