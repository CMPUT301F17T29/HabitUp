package com.example.habitup.View;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This activity will display all the user's current follow requests.
 */
public class FollowActivity extends BaseActivity {

    private ArrayList<String> requestList;
    private RecyclerView requestsListView;
    private FollowRequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_request);
        requestList = new ArrayList<>();

        requestsListView = (RecyclerView) findViewById(R.id.request_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        requestsListView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        requestsListView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestList.clear();

        if (!HabitUpApplication.isOnline(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    "Error: No connection to the internet.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        requestList = currentUser.getRequestList().getUserList();

        requestAdapter = new FollowRequestAdapter(this, requestList);
        requestsListView.setAdapter(requestAdapter);

        updateTotal();

        navigationView.setCheckedItem(R.id.requests);
    }

    public void updateTotal() {
        TextView requestsSubheading = (TextView) findViewById(R.id.requests_subheading);
        String subHeading1 = "You have " + requestList.size() + " friend requests.";
        String subHeading2 = "You have " + requestList.size() + " friend request.";

        if (requestList.size() == 1) {
            requestsSubheading.setText(subHeading2);
        } else {
            requestsSubheading.setText(subHeading1);
        }
    }

}
