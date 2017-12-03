package com.example.habitup.Model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ty on 2017-10-22.
 *
 * UserAccountList: container for UserAccounts, used to store AllUsers, FriendLists, FriendRequests
 * Implemented with an ArrayList
 *
 * Javadoc last updated 2017-11-13 by @gojeffcho
 */

public class UserAccountList {

    private ArrayList<UserWrapper> userList;

    /**
     * Constructor: create new ArrayList
     */
    public UserAccountList() {
        userList = new ArrayList<>();
    }

    /**
     * Get the array list of user accounts
     * @return the user accounts list
     */
    public ArrayList<UserWrapper> getUserList() {
        return this.userList;
    }

    /**
     * Get the number of UserAccounts in the UserAccountList
     * @return Int (number of elements in UserAccountList)
     */
    public int size() {
        return userList.size();
    }

    /**
     * Add a UserAccount to the list
     * @param userToAdd UserAccount username
     * @return -1 if already in list, 0 if successfully added
     */
    public int add(UserAccount userToAdd) {

        UserWrapper user = new UserWrapper(userToAdd);

        if (this.contains(user)) {
            return -1;
        } else {
            userList.add(user);
            return 0;
        }
    }

    /**
     * Removes a UserAccount from the list
     * @param userToRemove Username of UserAccount to delete
     * @return -1 if user is not in the list, 0 if successfully removes
     */
    public int delete(UserAccount userToRemove) {

        UserWrapper user = new UserWrapper(userToRemove);

        if (this.contains(user)) {
            Log.i("HabitUpDEBUG", "UAL - Try to remove: " + user.getUsername());
            userList.remove(user);
            return 0;
        } else {
            Log.i("HabitUpDEBUG", "UAL - No match for: " + user.getUsername());
            return -1;
        }
    }

    /**
     * Check whether UserAccountList contains a certain UserAccount
     * @param user UserAccount username
     * @return True if contained; otherwise False
     */
    public Boolean contains(UserWrapper user) {
        return userList.contains(user);
    }

    public Boolean contains(UserAccount user) {
        return userList.contains(new UserWrapper(user));
    }

}
