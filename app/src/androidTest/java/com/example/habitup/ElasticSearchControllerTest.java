package com.example.habitup;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Model.UserAccount;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;


/**
 * Created by Ty on 2017-11-08.
 */
@RunWith(AndroidJUnit4.class)
public class ElasticSearchControllerTest {

    /*@Test
    public void testAddUsersTask() {
        UserAccount u1 = new UserAccount("theise", "Tyler Heise", null);
        UserAccount u2 = new UserAccount("ezakirova", "E Z", null);

        //private ArrayList<UserAccount> userList = new ArrayList<UserAccount>();
        String x = u1.getUsername();
        String y = u2.getUsername();

        ElasticSearchController.AddUsersTask addUsersTask = new ElasticSearchController.AddUsersTask();

        addUsersTask.execute(u1);
        addUsersTask.execute(u2);
    }*/

    @Test
    public void testGetUsersTask(){

        ElasticSearchController.GetUserTask getUserTask = new ElasticSearchController.GetUserTask();

        ArrayList<UserAccount> users = new ArrayList<UserAccount>();

        getUserTask.execute("theise");

        try {
            users = getUserTask.get();
        }
        catch (Exception e)
        {
            Log.i("Error","Failed to get the User from the async object");
        }

        assert(users.get(0).getUsername().equals("theise"));
    }
}
