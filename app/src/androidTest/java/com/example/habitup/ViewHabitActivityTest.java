package com.example.habitup;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;

import com.example.habitup.View.AddHabitActivity;
import com.example.habitup.View.EditHabitActivity;
import com.example.habitup.View.ViewHabitActivity;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Tests viewing the Habits (ViewHabitActivity) along with testing AddHabit, ViewHabit, EditHabit,
 * and DeleteHabit functions from the view
 *
 * AddHabit - US 01.01.01, US 01.02.01, US 01.06.01
 * ViewHabit - US 01.03.01
 * EditHabit - US 01.04.01, US 01.06.01
 * DeleteHabit - US 01.05.01
 * by alido8592
 */

public class ViewHabitActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ViewHabitActivityTest() {
        super(ViewHabitActivity.class);
    }

    public void setUp() throws Exception {
        // get user info

        UserAccount user = new UserAccount("tatata2", "tatata2", null);
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("tatata2");

        try {
            user = getUser.get().get(0);
        } catch (Exception e) {
            HabitUpApplication.addUserAccount(user);
        }

        HabitUpApplication.setCurrentUser(user);

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testViewHabit() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));

        solo.assertCurrentActivity("Wrong activity", AddHabitActivity.class);
        solo.enterText(0, "swim");
        solo.enterText(1, "water life");
        solo.clickOnImage(0);
        solo.setDatePicker(0,2017,10,10);
        solo.clickOnText("OK");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertTrue(solo.waitForText("swim"));
        solo.clickOnText("swim");

        solo.assertCurrentActivity("Wrong activity", EditHabitActivity.class);
        assertTrue(solo.waitForText("water life"));
        solo.goBack();

        solo.clickLongOnTextAndPress("swim",1);
        solo.clickOnButton("Yes");
    }

    public void testAddHabit() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitActivity.class);

        solo.enterText(0, "exercise");
        solo.enterText(1, "swole goals");
        solo.clickOnImage(0);
        solo.setDatePicker(0,2017,10,10);
        solo.clickOnText("OK");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertTrue(solo.waitForText("exercise"));

        solo.clickLongOnTextAndPress("exercise",1);
        solo.clickOnButton("Yes");
    }

    public void testDeleteHabit() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitActivity.class);

        solo.enterText(0, "eat");
        solo.enterText(1, "gainz");
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

    public void testEditHabit() {
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.add_action_bar));
        solo.assertCurrentActivity("Wrong activity", AddHabitActivity.class);

        solo.enterText(0, "push ups");
        solo.enterText(1, "moar gainz");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertTrue(solo.waitForText("push ups"));

        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        solo.clickLongOnTextAndPress("push ups",0);
        solo.assertCurrentActivity("Wrong activity", EditHabitActivity.class);
        solo.clearEditText(0);
        solo.enterText(0, "push down");
        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong activity", ViewHabitActivity.class);
        assertTrue(solo.waitForText("push down"));
        solo.clickLongOnTextAndPress("push down",1);
        solo.clickOnButton("Yes");
    }
}
