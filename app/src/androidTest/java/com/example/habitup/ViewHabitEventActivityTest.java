package com.example.habitup;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.UserAccount;
import com.robotium.solo.Solo;

/**
 * Created by Ty on 2017-11-13.
 */

public class ViewHabitEventActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public ViewHabitEventActivityTest() {
        super(com.example.habitup.View.ViewHabitEventActivity.class);
    }

    /**Runs before starting tests
     *
     * @throws Exception
     */
    public void setUp() throws Exception{
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

        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * runs after tests
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
