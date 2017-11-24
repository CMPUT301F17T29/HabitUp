package com.example.habitup;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;
import com.example.habitup.View.FollowActivity;
import com.robotium.solo.Solo;

/**
 * Created by barboza on 2017-11-23.
 */

public class FriendRequestsTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public FriendRequestsTest() {
        super(FollowActivity.class);
    }

    public void setUp() throws Exception {
        UserAccount user = new UserAccount("testrequests", "testrequests", null);
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("testrequests");

        try {
            user = getUser.get().get(0);
        } catch (Exception e) {
            HabitUpApplication.addUserAccount(user);
        }

        HabitUpApplication.setCurrentUser(user);

        UserAccount requestUser = new UserAccount("user1", "test user 1", null);
        if (user.getRequestList().size() == 0) {
            HabitUpController.addFriendRequest(requestUser);
        }

        UserAccountList friendsList = user.getFriendsList();
        friendsList.getUserList().clear();
        HabitUpController.updateUser();

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testIgnoreRequest() {
        solo.assertCurrentActivity("Wrong activity", FollowActivity.class);

        RecyclerView followListView = (RecyclerView) solo.getView(R.id.request_listview);
        View requestView = followListView.getChildAt(0);
        Button ignoreButton = requestView.findViewById(R.id.ignore_button);
        solo.clickOnView(ignoreButton);

        assertTrue(solo.waitForText("You have 0 friend requests."));
        assertTrue(!solo.waitForText("user1"));

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        assertTrue(currentUser.getRequestList().size() == 0);
        assertTrue(currentUser.getFriendsList().size() == 0);
    }

    public void testAcceptRequest() {
        solo.assertCurrentActivity("Wrong activity", FollowActivity.class);

        RecyclerView followListView = (RecyclerView) solo.getView(R.id.request_listview);
        View requestView = followListView.getChildAt(0);
        Button acceptButton = requestView.findViewById(R.id.accept_button);
        solo.clickOnView(acceptButton);

        assertTrue(solo.waitForText("You have 0 friend requests."));
        assertTrue(!solo.waitForText("user1"));

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        assertTrue(currentUser.getRequestList().size() == 0);

        UserAccountList friendsList = currentUser.getFriendsList();
        assertTrue(friendsList.size() == 1);
        assertTrue(friendsList.getUserList().get(0).getUsername().equals("user1"));
    }
}
