package com.example.habitup.View;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This activity will display all the user's current follow requests.
 */
public class FollowActivity extends AppCompatActivity{
    private ArrayList requestList = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_request);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FollowRequestAdapter adapter = new FollowRequestAdapter(requestList);
        recyclerView.setAdapter(adapter);



    }

}
