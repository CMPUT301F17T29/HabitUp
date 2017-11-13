package com.example.habitup;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;


import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;

import com.example.habitup.View.AddHabitActivity;
import com.example.habitup.View.ViewHabitActivity;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Collections;


public class ViewHabitActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ViewHabitActivityTest() {
        super(ViewHabitActivity.class);
    }

    public void setUp() throws Exception {
        // get user info
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("tatata");

        UserAccount user = new UserAccount("tatata","tatata",null);
        try {
            user = getUser.get().get(0);
        }
        catch (Exception e) {
            //nothing here
        }

        HabitUpApplication.setCurrentUser(user);

        // get user habits
        ElasticSearchController.GetHabitsTask getHabits = new ElasticSearchController.GetHabitsTask();
        getHabits.execute("tatata");
        ArrayList<Habit> habits = new ArrayList<Habit>();
        try {
            habits.addAll(getHabits.get());
            Collections.sort(habits);
        }
        catch (Exception e) {
            Log.d("Error", "Failed to retrieve habits from ES");
        }

        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testViewHabit() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertTrue(solo.waitForText("Habits"));
    }

    public void testAddHabit() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitActivity.class);

        solo.enterText((EditText) solo.getView(R.id.habit_name), "exercise");
        solo.enterText((EditText) solo.getView(R.id.habit_reason), "swole goals");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertTrue(solo.waitForText("exercise"));
    }

    public void testDeleteHabit() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitActivity.class);

        solo.enterText((EditText) solo.getView(R.id.habit_name), "eat");
        solo.enterText((EditText) solo.getView(R.id.habit_reason), "gainz");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertTrue(solo.waitForText("eat"));

        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickLongOnTextAndPress("eat",1);
        solo.clickOnButton("Yes");
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertFalse(solo.waitForText("eat"));
    }
}
