package com.example.habitup.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserWrapper;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This activity displays a list of the user's friends and their habits.
 *
 * @author Shari Barboza
 */
public class ViewFriendsActivity extends BaseActivity {

    private ArrayList<String> friends;
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
        for (UserWrapper friend : currentUser.getFriendsList().getUserList()) {
            friends.add(friend.getUsername());
        }

        friendsAdapter = new FriendsListAdapter(this, friends);
        friendsListView.setAdapter(friendsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        friendsListView.setLayoutManager(layoutManager);

        Button findButton = (Button) findViewById(R.id.find_user_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findIntent = new Intent(ViewFriendsActivity.this, FindUserActivity.class);
                startActivity(findIntent);
            }
        });
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
