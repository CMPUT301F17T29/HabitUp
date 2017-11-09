package com.example.habitup;

import com.example.habitup.Controller.ElasticSearchController;

import com.example.habitup.Model.UserAccount;


/**
 * Created by Ty on 2017-11-08.
 */

public class ElasticSearchControllerTest {

    private UserAccount u1 = new UserAccount("theise", "Tyler Heise", null);
    private UserAccount u2 = new UserAccount("ezakirova", "E Z", null);

    //private ArrayList<UserAccount> userList = new ArrayList<UserAccount>();
    String x = u1.getUsername();
    String y = u2.getUsername();

    ElasticSearchController.AddUsersTask addUsersTask = new ElasticSearchController.AddUsersTask();

    addUsersTask.execute(u1);

}
