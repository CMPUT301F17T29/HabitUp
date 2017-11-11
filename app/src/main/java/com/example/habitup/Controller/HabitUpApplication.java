package com.example.habitup.Controller;


import android.widget.Toast;

import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;

public class HabitUpApplication {

    static UserAccount currentUser;
    static Boolean setupDone = Boolean.FALSE;

    static public UserAccount getCurrentUser() { return currentUser; }
    static public int getCurrentUID() { return currentUser.getUID(); }

    public void testAccount() {
        // DEBUG
        if (!setupDone) {
            currentUser = new UserAccount("gojeffcho", "Jeff Cho", null);
            currentUser.getAttributes().setValue("Mental", 5);
            currentUser.getAttributes().setValue("Discipline", -10);
            currentUser.increaseXP(4);
            setupDone = Boolean.TRUE;
        }
    }

    static public void setCurrentUser(UserAccount user) { currentUser = user; }

    static public int addUserAccount(UserAccount user) {

        // Try: ElasticSearch put user
        ElasticSearchController.AddUsersTask addUser = new ElasticSearchController.AddUsersTask();
        addUser.execute(user);

        ElasticSearchController.AddAttrsTask addAttr = new ElasticSearchController.AddAttrsTask();
        Attributes attrs = new Attributes(user.getUID());
//        attrs.setValue("Mental", 5); // Test
        addAttr.execute(attrs);

        return 0;
    }

    static public UserAccountList getAllUserAccounts() {

        UserAccountList userList = new UserAccountList();

        // ElasticSearch for all users, add to userList
//        ElasticSearchController.GetAllUsersTask allUsers = new ElasticSearchController.GetAllUsersTask();
//        allUsers.execute();

        // Add to userlist

        return userList;
    }

    static public int getUserAccount(String username) {

        // ElasticSearch for username

        // if we got it, success

        // if not, failure

        return 0;
    }

}
