package com.example.habitup.View;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

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
    private RecyclerView habitsListView;
    private FriendsListAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Initialize friends list view
        friendsListView = (RecyclerView) findViewById(R.id.friends_listview);
        friendsListView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        friendsListView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: Get user's friend list from ES Controller
        friends = new ArrayList<>();

        friendsAdapter = new FriendsListAdapter(this, friends);
        friendsListView.setAdapter(friendsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        friendsListView.setLayoutManager(layoutManager);

        friendsAdapter.notifyDataSetChanged();

        if (friends.size() == 0) {
            TextView subHeading = (TextView) findViewById(R.id.friends_subheading);
            subHeading.setText("You currently have no friends.");
        }

        navigationView.setCheckedItem(R.id.friends);
    }
}
