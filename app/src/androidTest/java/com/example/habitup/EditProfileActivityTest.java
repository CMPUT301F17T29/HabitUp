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

public class EditProfileActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public EditProfileActivityTest() {
        super(com.example.habitup.View.EditProfileActivity.class);
    }

    /**Runs before starting tests
     *
     * @throws Exception
     */
    public void setUp() throws Exception{
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

        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testName() throws Exception{

    }

    public void testPhoto() throws Exception{

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
