package com.example.habitup.View;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This activity will search for other users by username or full name. A user can
 * send a follow request to any of the users they find.
 */
public class FindUserActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    ArrayList<UserAccount> allUsers;
    ArrayList<UserAccount> resultsList;
    RecyclerView resultsListView;
    FindUserAdapter resultsAdapter;
    TextView resultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_user);
        allUsers = new ArrayList<>();
        resultsList = new ArrayList<>();

        resultCount = (TextView) findViewById(R.id.results_found);

        resultsListView = (RecyclerView) findViewById(R.id.search_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultsListView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        resultsListView.addItemDecoration(itemDecoration);

        // Set up Search View
        SearchView searchFriends = (SearchView) findViewById(R.id.user_search);
        searchFriends.setQueryHint("Search for a user");
        searchFriends.setIconifiedByDefault(false);
        searchFriends.setOnQueryTextListener(this);

        // Get all users
        ElasticSearchController.GetAllUsers getUsersTask = new ElasticSearchController.GetAllUsers();
        getUsersTask.execute();

        try {
            ArrayList<UserAccount> tempUsers = getUsersTask.get();
            allUsers.addAll(tempUsers);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "Failed to get all users.");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        resultsList.clear();

        resultsList.addAll(allUsers);
        resultsAdapter = new FindUserAdapter(this, resultsList);
        resultsListView.setAdapter(resultsAdapter);

        updateResultsCount();

        navigationView.setCheckedItem(R.id.finds);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        String query = searchText.toLowerCase();
        ;
        ArrayList<UserAccount> tempList = new ArrayList<>();

        // Check in existing results list when chaining letters
        for (int i = 0; i < resultsList.size(); i++) {
            UserAccount user = resultsList.get(i);
            String nickName = user.getRealname().toLowerCase();
            String userName = user.getUsername().toLowerCase();

            if (nickName.contains(query) || userName.contains(query)) {
                tempList.add(user);
            }
        }

        resultsList.clear();
        if (query.length() == 0) {
            resultsList.addAll(allUsers);
        } else {
            resultsList.addAll(tempList);
        }

        resultsAdapter.notifyDataSetChanged();
        updateResultsCount();

        return false;
    }

    private void updateResultsCount() {
        String results1 = "1 result found.";
        String results2 = resultsList.size() + " results found.";

        if (resultsList.size() == 1) {
            resultCount.setText(results1);
        } else {
            resultCount.setText(results2);
        }
    }
}
