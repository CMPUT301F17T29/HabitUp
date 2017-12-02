package com.example.habitup.Model;

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

    private ArrayList<Integer> userList;

    /**
     * Constructor: create new ArrayList
     */
    public UserAccountList() {

        userList = new ArrayList<>();
        userList.add(0);
    }

    /**
     * Get the array list of user accounts
     * @return the user accounts list
     */
    public ArrayList<Integer> getUserList() {
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
     * @param uidToAdd UserAccount username
     * @return -1 if already in list, 0 if successfully added
     */
    public int add(Integer uidToAdd) {

        if (this.contains(uidToAdd)) {
            return -1;
        } else {
            userList.add(uidToAdd);
            return 0;
        }
    }

    /**
     * Removes a UserAccount from the list
     * @param uidToRemove Username of UserAccount to delete
     * @return -1 if user is not in the list, 0 if successfully removes
     */
    public int delete(Integer uidToRemove) {

        if (this.contains(uidToRemove)) {
            userList.remove(uidToRemove);
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Check whether UserAccountList contains a certain UserAccount
     * @param user UserAccount username
     * @return True if contained; otherwise False
     */
    public Boolean contains(Integer user) {
        return userList.contains(user);
    }

}
