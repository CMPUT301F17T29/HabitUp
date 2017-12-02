package com.example.habitup.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

/**
 * This is the main parent activity class for other activities that need access to the
 * drawer navigation layout. Here, the initialization for the drawer will occur.
 *
 * @author Shari Barboza
 */
public class BaseActivity extends AppCompatActivity {

    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Context context = BaseActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        LinearLayout container = fullView.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, container, true);

        super.setContentView(fullView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavigationDrawer();
    }

    public void initNavigationDrawer() {

        // Taken from https://www.learn2crack.com/2016/03/android-material-design-sliding-navigation-drawer.html
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.profile:
                        Intent profileIntent = new Intent(context, MainActivity.class);
                        startActivity(profileIntent);
                        break;
                    case R.id.habits:
                        Intent habitsIntent = new Intent(context, ViewHabitActivity.class);
                        startActivity(habitsIntent);
                        break;
                    case R.id.events:
                        Intent eventsIntent = new Intent(context, ViewHabitEventActivity.class);
                        startActivity(eventsIntent);
                        break;
                    case R.id.friends:
                        Intent friendsIntent = new Intent(context, ViewFriendsActivity.class);
                        startActivity(friendsIntent);
                        break;
                    case R.id.requests:
                        Intent requestsIntent = new Intent(context, FollowActivity.class);
                        startActivity(requestsIntent);
                        break;
                    case R.id.finds:
                        Intent findIntent = new Intent(context, FindUserActivity.class);
                        startActivity(findIntent);
                        break;
                    case R.id.map:
                        Intent mapsIntent = new Intent(context, MapsActivity.class);
                        startActivity(mapsIntent);
                        break;
                    case R.id.logout:
                        logout();
                        break;
                }

                return true;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

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

        drawerLayout.closeDrawers();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set the profile name and pic
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        String fullName = currentUser.getRealname();
        Bitmap profilePic = currentUser.getPhoto();

        // Get the nav header view
        View header = navigationView.getHeaderView(0);

        // Set the user's full name
        TextView nameView = header.findViewById(R.id.drawer_name);
        nameView.setText(fullName);

        // Set the user's profile photo
        if (profilePic != null) {
            ImageView photoView = header.findViewById(R.id.drawer_pic);
            photoView.setImageBitmap(profilePic);
        }
    }

    private void logout() {
        Toast.makeText(getApplicationContext(), HabitUpApplication.getCurrentUser().getUsername() + " has logged out", Toast.LENGTH_LONG).show();
        HabitUpApplication.logoutCurrentUser();
        Intent logoutIntent = new Intent(context, LoginActivity.class);
        startActivity(logoutIntent);
    }

}