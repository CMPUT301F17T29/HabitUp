package com.example.habitup;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.FollowController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.View.FollowActivity;
import com.example.habitup.View.LoginActivity;
import com.example.habitup.View.MainActivity;
import com.example.habitup.View.ViewFriendsActivity;
import com.robotium.solo.Solo;

/**
 * Created by barboza on 2017-11-23.
 */

public class FriendRequestsTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;
    private UserAccount user;
    private UserAccount requestUser;

    public FriendRequestsTest() {
        super(FollowActivity.class);
    }

    public void setUp() throws Exception {

        user = new UserAccount("tatata4", "tatata4", null);
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("tatata4");

        try {
            user = getUser.get().get(0);
        } catch (Exception e) {
            HabitUpApplication.addUserAccount(user);
        }

        HabitUpApplication.setCurrentUser(user);

        requestUser = new UserAccount("user1", "test user 1", null);
        ElasticSearchController.GetUser getRequest = new ElasticSearchController.GetUser();
        getRequest.execute("user1");

        try {
            requestUser = getRequest.get().get(0);
        } catch (Exception e) {
            HabitUpApplication.addUserAccount(requestUser);
        }

        requestUser.getFriendsList().getUserList().clear();
        HabitUpApplication.updateUser(requestUser);

        if (user.getRequestList().size() == 0) {
            FollowController.addFriendRequest(user, requestUser);
        }

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testIgnoreRequest() {
        solo.assertCurrentActivity("Wrong activity", FollowActivity.class);
        solo.clickOnButton("Ignore");

        assertTrue(solo.waitForText("You have 0 friend requests."));
        assertTrue(!solo.waitForText("user1"));

        solo.clickOnImageButton(0);
        solo.clickOnText("Log Out");
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText(0,"user1");
        solo.clickOnButton("Login");

        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Friends");

        solo.assertCurrentActivity("Wrong activity", ViewFriendsActivity.class);
        assertTrue(solo.waitForText("You currently have no friends."));
    }

    public void testAcceptRequest() {
        solo.assertCurrentActivity("Wrong activity", FollowActivity.class);
        solo.clickOnButton("Accept");

        assertTrue(solo.waitForText("You have 0 friend requests."));
        assertTrue(!solo.waitForText("user1"));

        solo.clickOnImageButton(0);
        solo.clickOnText("Log Out");
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText(0,"user1");
        solo.clickOnButton("Login");

        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Friends");

        solo.assertCurrentActivity("Wrong activity", ViewFriendsActivity.class);
        assertTrue(solo.waitForText("tatata4"));
    }
}
