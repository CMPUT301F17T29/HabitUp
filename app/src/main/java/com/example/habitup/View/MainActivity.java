package com.example.habitup.View;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

        Habit habit1 = new Habit();
        habit1.setHabitName("Go to the gym");
        habit1.setAttribute("Physical");
        habitList.add(habit1);
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

        // Highlight profile list item
        navigationView.getMenu().getItem(0).setChecked(true);

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
