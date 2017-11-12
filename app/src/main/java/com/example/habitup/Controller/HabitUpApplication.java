package com.example.habitup.Controller;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;

public class HabitUpApplication {

    public final static int MAX_USERNAME_LENGTH = 15;
    public final static int MAX_REALNAME_LENGTH = 20;
    public static final int MAX_PHOTO_BYTECOUNT = 65536;
    public final static int XP_INCREASE_AMOUNT = 25;
    public final static int XP_PER_HABITEVENT = 1;
    public final static int ATTR_INCREMENT_PER_HABITEVENT = 1;

    static UserAccount currentUser;
    static Attributes currentAttrs;
    static Boolean setupDone = Boolean.FALSE;

    public static int NUM_OF_ES_RESULTS = 50;

    static public UserAccount getCurrentUser() { return currentUser; }
    static public int getCurrentUID() { return currentUser.getUID(); }
    static public String getCurrentUIDAsString() { return String.valueOf(currentUser.getUID()); }

    static public Attributes getCurrentAttrs() { return currentAttrs; }

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

    static public void setCurrentUser(UserAccount user) {
        currentUser = user;
        updateCurrentAttrs();
    }

    static public void updateCurrentAttrs() {
        ElasticSearchController.GetAttributesTask attrs = new ElasticSearchController.GetAttributesTask();
        attrs.execute(String.valueOf(currentUser.getUID()));
        try {
            currentAttrs = attrs.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUApp/Couldn't get Attributes for user");
        }
    }

    static public int addUserAccount(UserAccount user) {

        // Try: ElasticSearch put user
        ElasticSearchController.AddUsersTask addUser = new ElasticSearchController.AddUsersTask();
        addUser.execute(user);

        ElasticSearchController.AddAttrsTask addAttr = new ElasticSearchController.AddAttrsTask();
        Attributes attrs = new Attributes(user.getUID());
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

    static public UserAccount getUserAccount(String username) {

        // ElasticSearch for username
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);
        UserAccount user;

        try {
            user = getUser.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUApp/getUserAccount - failed");
            return null;
        }

        return user;
    }

    /**
     * check whether Internet is connected
     */
    static public boolean isOnline(Context ctx) {
        // Taken from: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        // 2017-11-11
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
