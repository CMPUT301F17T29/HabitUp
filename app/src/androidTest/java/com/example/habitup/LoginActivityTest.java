package com.example.habitup;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.View.LoginActivity;
import com.example.habitup.View.MainActivity;
import com.robotium.solo.Solo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Shuyang on 2017-11-12.
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private String testName;

    public LoginActivityTest(){ super(LoginActivity.class);}
    @Test
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }
    @Test
    public void testStart() throws Exception{
        AppCompatActivity activity = getActivity();
    }
    @Test
    //Test if the user can enter main page after login using a valid user name
    public void testLogin() {
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        while (true){
            Random rand = new Random();
            testName = Integer.toString(rand.nextInt(9999));
            ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
            getUser.execute(testName);
            ArrayList<UserAccount> users = new ArrayList<>();
            try {
                users = getUser.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get the User from the async object");
            }
            if (users.size() == 0) {
                break;
            }

        }
        UserAccount u1 = new UserAccount(testName, "usernametest", null);

        ElasticSearchController.AddUsersTask addUsersTask = new ElasticSearchController.AddUsersTask();
        addUsersTask.execute(u1);

        SystemClock.sleep(10000);

        //check whether we add that user
        solo.enterText((EditText) solo.getView(R.id.login_edit), testName);
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

    }
}
