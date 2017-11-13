package com.example.habitup;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;


import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.View.AddHabitEventActivity;
import com.example.habitup.View.EditHabitEventActivity;
import com.example.habitup.View.ViewHabitEventActivity;
import com.robotium.solo.Solo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;


public class AddHabitEventTest extends ActivityInstrumentationTestCase2{
    private Solo solo;

    public AddHabitEventTest() {
        super(ViewHabitEventActivity.class);
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
            if (habits.size() == 0) {
                Habit h = new Habit(HabitUpApplication.getCurrentUID());
                h.setHabitName("live");
                h.setReason("YOLO");
                h.setAttribute("Physical");
                h.setStartDate(LocalDate.of(2016,10,20));
                boolean[] schedule = new boolean[8];
                schedule[0] = false;
                schedule[1] = true;
                schedule[2] = true;
                schedule[3] = true;
                schedule[4] = true;
                schedule[5] = true;
                schedule[6] = true;
                schedule[7] = true;
                h.setSchedule(schedule);
                HabitUpController.addHabit(h);

            }
        }
        catch (Exception e) {
            Log.d("Error", "Failed to retrieve habits from ES");
        }

        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testAddHabitEvent() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitEventActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitEventActivity.class);

        solo.pressSpinnerItem(0,0);
        solo.clickOnImage(0);
        solo.setDatePicker(0,2017,10,10);
        solo.clickOnText("OK");
        solo.enterText(0, "I lived");
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong activity", ViewHabitEventActivity.class);
        assertTrue(solo.waitForText("I lived"));

        solo.clickLongOnTextAndPress("I lived",1);
        solo.clickOnButton("Yes");

    }

    public void testViewHabitEvent() {
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitEventActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewHabitEventActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitEventActivity.class);

        solo.pressSpinnerItem(0,0);
        solo.clickOnImage(0);
        solo.setDatePicker(0,2017,10,10);
        solo.clickOnText("OK");
        solo.enterText(0, "I lived");
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong activity", ViewHabitEventActivity.class);
        assertTrue(solo.waitForText("I lived"));
        solo.clickOnText("I lived");
        solo.assertCurrentActivity("Wrong activity", EditHabitEventActivity.class);
        solo.goBack();

        solo.clickLongOnTextAndPress("I lived",1);
        solo.clickOnButton("Yes");
    }
}
