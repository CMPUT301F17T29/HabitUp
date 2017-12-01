package com.example.habitup;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.FollowController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventList;
import com.example.habitup.Model.HabitList;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;
import com.example.habitup.View.EditHabitEventActivity;
import com.example.habitup.View.LoginActivity;
import com.example.habitup.View.MainActivity;
import com.example.habitup.View.ViewFriendsActivity;
import com.robotium.solo.Solo;

import java.time.LocalDate;

/**
 * Created by barboza on 2017-11-24.
 */

public class ViewFriendsActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ViewFriendsActivityTest() {
        super(ViewFriendsActivity.class);
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

        UserAccountList friendsList = user.getFriendsList();
        friendsList.getUserList().clear();
        HabitUpController.updateUser();

        UserAccount friend1 = new UserAccount("user2", "user 2", null);

        Habit habit1 = new Habit(1);
        habit1.setHabitName("Go to the gym");
        habit1.setAttribute("Physical");

        HabitEvent event1 = new HabitEvent(user.getUID(), habit1.getHID());
        event1.setHabitStrings(habit1);
        event1.setCompletedate(LocalDate.of(2017, 11, 24));

        HabitEvent event2 = new HabitEvent(user.getUID(), habit1.getHID());
        event2.setHabitStrings(habit1);
        event2.setCompletedate(LocalDate.of(2017, 11, 23));

        HabitEventList eventList = friend1.getEventList();
        eventList.add(event1);
        eventList.add(event2);

        Habit habit2 = new Habit(2);
        habit2.setHabitName("Clean room");
        habit2.setAttribute("Discipline");

        HabitList habitList = friend1.getHabitList();
        habitList.add(habit1);
        habitList.add(habit2);
        HabitUpApplication.updateUser(friend1);

        FollowController.addFriend(friend1, user);

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testViewFriendHabits() {
        solo.assertCurrentActivity("Wrong activity", ViewFriendsActivity.class);
        assertTrue(solo.waitForText("user2"));

        assertTrue(!solo.waitForText("Go to the gym"));
        assertTrue(!solo.waitForText("Clean room"));

        solo.clickOnText("user1");
        assertTrue(solo.waitForText("Go to the gym"));
        assertTrue(solo.waitForText("Clean room"));

        RecyclerView friendsListView = (RecyclerView) solo.getView(R.id.friends_listview);

        View habitView1 = friendsListView.getChildAt(1);
        TextView habit1 = habitView1.findViewById(R.id.friend_habit_name);
        assertTrue(habit1.getText().equals("Clean room"));

        View habitView2 = friendsListView.getChildAt(2);
        TextView habit2 = habitView2.findViewById(R.id.friend_habit_name);
        assertTrue(habit2.getText().equals("Go to the gym"));

        solo.clickOnText("user2");
        assertTrue(!solo.waitForText("Go to the gym"));
        assertTrue(!solo.waitForText("Clean room"));
    }

    public void testHabitWithNoRecentEvent() {
        solo.assertCurrentActivity("Wrong activity", ViewFriendsActivity.class);
        assertTrue(solo.waitForText("user2"));

        solo.clickOnText("user2");
        assertTrue(solo.waitForText("Clean room"));
        solo.clickOnText("Clean room");

        solo.assertCurrentActivity("Wrong activity", ViewFriendsActivity.class);
    }

    public void testHabitWithMostRecentEvent() {
        solo.assertCurrentActivity("Wrong activity", ViewFriendsActivity.class);
        assertTrue(solo.waitForText("user2"));

        solo.clickOnText("user2");
        assertTrue(solo.waitForText("Go to the gym"));
        solo.clickOnText("Go to the gym");

        solo.assertCurrentActivity("Wrong activity", EditHabitEventActivity.class);
        assertTrue(solo.waitForText("Go to the gym"));
        assertTrue(solo.waitForText("Nov 24, 2017"));

        solo.clickOnActionBarHomeButton();
        solo.assertCurrentActivity("Wrong activity", ViewFriendsActivity.class);
        assertTrue(solo.waitForText("Go to the gym"));
        assertTrue(solo.waitForText("Clean room"));
    }
}
