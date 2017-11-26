package com.example.habitup.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventList;
import com.example.habitup.Model.HabitList;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;
import com.example.habitup.R;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This activity displays a list of the user's friends and their habits.
 *
 * @author Shari Barboza
 */
public class ViewFriendsActivity extends BaseActivity {

    public static final int VIEW_FRIEND_HABIT = 4;

    private ArrayList<UserAccount> friends;
    private RecyclerView friendsListView;
    private FriendsListAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        friends = new ArrayList<>();

        // Initialize friends list view
        friendsListView = (RecyclerView) findViewById(R.id.friends_listview);
        friendsListView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        friendsListView.addItemDecoration(itemDecoration);

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        friends = currentUser.getFriendsList().getUserList();

        friendsAdapter = new FriendsListAdapter(this, friends);
        friendsListView.setAdapter(friendsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        friendsListView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (friends.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.friends_subheading);
            subHeading.setText("You currently have no friends.");
        }

        navigationView.setCheckedItem(R.id.friends);
    }
}
