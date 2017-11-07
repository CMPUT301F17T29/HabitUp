package com.example.habitup.Model;

import java.util.ArrayList;

/**
 * Created by Ty on 2017-10-22.
 *
 * UserAccountList: container for UserAccounts, used in HabitUp app level class
 * Implemented with an ArrayList
 */

public class UserAccountList {
    //Members
    private ArrayList<UserAccount> userList;

    /**
     * Constructor: create new ArrayList
     */
    public UserAccountList() {
        userList = new ArrayList<UserAccount>();
    }

    /**
     * Get the number of UserAccounts in the UserAccountList
     * @return Int
     */
    public int size() {
        return userList.size();
    }

    /**
     * Add a UserAccount to the list
     * @param userToAdd
     * @return -1 if already in list, 0 if successfully added
     */
    public int add(UserAccount userToAdd) {

        if (this.contains(userToAdd)) {
            return -1;
        } else {
            userList.add(userToAdd);
            return 0;
        }
    }

    /**
     * Check whether UserAccountList contains a certain UserAccount
     * @param user
     * @return True if contained; otherwise False
     */
    public Boolean contains(UserAccount user) {
        return userList.contains(user);
    }

    /**
     * Remove a HabitEvent from the HabitEventList if it exists
     * @param user
     */
    public void remove(UserAccount user) {
        userList.remove(user);
    }

    /**
     * Not sure if this is actually need yet
     * Return an ArrayList of UserAccounts represented by the UserAccountList
     * @return
     */
    public ArrayList<UserAccount> getUserAccounts() {
        ArrayList<UserAccount> returnList = new ArrayList<>(userList.size());
        for (UserAccount u : userList) {
            returnList.add(u);
        }
        return returnList;
    }

    /**
     * Return the UserAccount represented by the specified username
     * @param username
     * return
     */

    public UserAccount getUser(String username){
        //placeholder
        return null;
    }

    /**
     * Return an ArrayList of UserAccounte who match the searchTerm
     * @param searchTerm
     * @return
     */
    public UserAccountList searchUsers(String searchTerm){
        //placeholder
        return null;
    }



}
