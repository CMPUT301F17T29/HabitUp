package com.example.habitup.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;

/**
 * HabitUpApplication is the controller at the app-level.  It deals with functionality at the
 * highest level, including checking for connectivity, dealing with logins, and anything to do with
 * other users such as friends and following.  It is also the store of static variables that are
 * useful to be stored at the app level.
 *
 * @author gojeffcho
 *
 * Javadoc last updated 2017-11-13 by @gojeffcho
 */

public class HabitUpApplication {

    // Static 'global' variables
    public final static int MAX_USERNAME_LENGTH = 15;
    public final static int MAX_REALNAME_LENGTH = 25;
    public static final int MAX_PHOTO_BYTECOUNT = 65536;
    public final static int XP_INCREASE_AMOUNT = 25;
    public final static int XP_PER_HABITEVENT = 1;
    public final static int ATTR_INCREMENT_PER_HABITEVENT = 1;

    static UserAccount currentUser;
    static Attributes currentAttrs;

    public static int NUM_OF_ES_RESULTS = 50;
    public static int NUM_OF_ES_RESULTS_FOR_DELETE = 99999;

    /**
     * Get the current logged-in UserAccount
     * @return UserAccount of logged-in user
     */
    static public UserAccount getCurrentUser() { return currentUser; }

    /**
     * Get the UID of the logged-in user
     * @return int UID
     */
    static public int getCurrentUID() { return currentUser.getUID(); }

    /**
     * Get the UID of the logged-in user as String; useful for ElasticSearch queries
     * @return String UID
     */
    static public String getCurrentUIDAsString() { return String.valueOf(currentUser.getUID()); }

    /**
     * Gets the Attributes object of the current logged-in user.
     * @return Attributes
     */
    static public Attributes getCurrentAttrs() { return currentAttrs; }

    /**
     * Utility method to clear the current login
     */
    static public void logoutCurrentUser() {
        currentUser = null;
        currentAttrs = null;
    }

    /**
     * Utility method to set the current logged-in UserAccount and get their Attributes object
     * @param user (UserAccount)
     */
    static public void setCurrentUser(UserAccount user) {
        currentUser = user;
        updateCurrentAttrs();
    }

    /**
     * Utility method to set the static Attributes object belonging to the current logged-in user
     */
    static public void updateCurrentAttrs() {
        ElasticSearchController.GetAttributesTask attrs = new ElasticSearchController.GetAttributesTask();
        attrs.execute(String.valueOf(currentUser.getUID()));
        try {
            currentAttrs = attrs.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUApp/Couldn't get Attributes for user");
        }
    }

    /**
     * Creating a new UserAccount, and an Attributes object to go with it, since it is new
     * @param user UserAccount of new user
     * @return 0 for success
     */
    static public int addUserAccount(UserAccount user) {

        // Try: ElasticSearch put user
        ElasticSearchController.AddUsersTask addUser = new ElasticSearchController.AddUsersTask();
        addUser.execute(user);

        ElasticSearchController.AddAttrsTask addAttr = new ElasticSearchController.AddAttrsTask();
        Attributes attrs = new Attributes(user.getUID());
        addAttr.execute(attrs);

        return 0;
    }

    /**
     * Gets all UserAccounts stored in the DB.  Used for inter-user functionality such as friends.
     * TODO: IMPLEMENT
     * @return UserAccountList of all users.
     */
    static public UserAccountList getAllUserAccounts() {

        UserAccountList userList = new UserAccountList();

        // ElasticSearch for all users, add to userList
//        ElasticSearchController.GetAllUsersTask allUsers = new ElasticSearchController.GetAllUsersTask();
//        allUsers.execute();

        // Add to userlist

        return userList;
    }

    /**
     * Look up a UserAccount by username.
     * @param username String of username to search for
     * @return UserAccount of matched username
     */
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
     * Taken from: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     * 2017-11-11
     */
    static public boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
