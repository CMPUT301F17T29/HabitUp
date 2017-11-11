package com.example.habitup;

import android.support.test.runner.AndroidJUnit4;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Model.UserAccount;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

/**
 * Created by gojeffcho on 2017-11-09.
 */

@RunWith(AndroidJUnit4.class)
public class JeffTest {

    @Test
    public void testAddUsersTask() {
        UserAccount u1 = new UserAccount("gojeffcho", "Jeff Cho", null);

        ElasticSearchController.AddUsersTask addUsersTask = new ElasticSearchController.AddUsersTask();

        addUsersTask.execute(u1);
        
    }

}