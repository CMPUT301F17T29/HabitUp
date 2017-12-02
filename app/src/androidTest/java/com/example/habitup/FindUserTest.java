package com.example.habitup;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.FollowController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.View.FindUserActivity;
import com.example.habitup.View.FollowActivity;
import com.example.habitup.View.LoginActivity;
import com.example.habitup.View.MainActivity;
import com.robotium.solo.Solo;

/**
 * Created by barboza on 2017-11-26.
 */

public class FindUserTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;
    private UserAccount user;
    private UserAccount user2;
    private UserAccount user3;

    public FindUserTest() {
        super(FindUserActivity.class);
    }

    public void setUp() throws Exception {
        user = new UserAccount("testrequests2", "testrequests2", null);
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("testrequests2");

        try {
            user = getUser.get().get(0);
        } catch (Exception e) {
            HabitUpApplication.addUserAccount(user);
        }
        HabitUpApplication.setCurrentUser(user);

        user2 = new UserAccount("andrew2", "andy", null);
        user3 = new UserAccount("anna2", "annie", null);

        ElasticSearchController.GetUser getUser2 = new ElasticSearchController.GetUser();
        getUser2.execute("andrew2");
        try {
            user2 = getUser2.get().get(0);
        } catch (Exception e) {
            HabitUpApplication.addUserAccount(user2);
        }

        ElasticSearchController.GetUser getUser3 = new ElasticSearchController.GetUser();
        getUser3.execute("anna2");
        try {
            user3 = getUser3.get().get(0);
        } catch (Exception e) {
            HabitUpApplication.addUserAccount(user3);
        }

        user.getFriendsList().getUserList().clear();
        FollowController.addFriend(user3, user);
        HabitUpController.updateUser();

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testUserNameSearch() {
        solo.assertCurrentActivity("Wrong activity", FindUserActivity.class);
        solo.enterText(0, "an");
        assertTrue(solo.waitForText("andrew2"));
        assertTrue(solo.waitForText("anna2"));

        solo.enterText(0, "d");
        assertTrue(solo.waitForText("andrew2"));
        assertTrue(!solo.waitForText("anna2"));
    }

    public void testRealNameSearch() {
        solo.assertCurrentActivity("Wrong activity", FindUserActivity.class);
        solo.enterText(0, "n");
        assertTrue(solo.waitForText("andrew2"));
        assertTrue(solo.waitForText("anna2"));

        solo.enterText(0, "i");
        assertTrue(solo.waitForText("anna2"));
        assertTrue(!solo.waitForText("andrew2"));
    }

    public void testFollowUser() {
        solo.assertCurrentActivity("Wrong activity", FindUserActivity.class);
        solo.enterText(0, "andrew2");

        solo.clickOnButton("Follow");
        assertTrue(solo.waitForText("A request was sent to andy"));
        assertFalse(solo.searchButton("Follow", true));

        solo.clickOnImageButton(0);
        solo.clickOnText("Log Out");
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        EditText login = (EditText) solo.getView(R.id.login_edit);
        solo.enterText(login, "andrew2");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Friend Requests");
        solo.assertCurrentActivity("Wrong activity", FollowActivity.class);
        assertTrue(solo.waitForText("testrequests2"));
    }

    public void testAlreadyFollowing() {
        solo.assertCurrentActivity("Wrong activity", FindUserActivity.class);

        solo.enterText(0, "anna2");
        assertFalse(solo.searchButton("Follow", true));
    }

    public void testFollowCurrentUser() {
        solo.assertCurrentActivity("Wrong activity", FindUserActivity.class);
        solo.enterText(0, "testrequests2");

        solo.clickOnButton("Follow");
        assertTrue(solo.waitForText("You cannot follow yourself"));
    }

    public void tearDown() throws Exception {
        user.getRequestList().getUserList().clear();
        user.getFriendsList().getUserList().clear();
        HabitUpApplication.updateUser(user);

        user2.getRequestList().getUserList().clear();
        user2.getFriendsList().getUserList().clear();
        HabitUpApplication.updateUser(user2);

        user3.getRequestList().getUserList().clear();
        user3.getFriendsList().getUserList().clear();
        HabitUpApplication.updateUser(user3);
    }
}
