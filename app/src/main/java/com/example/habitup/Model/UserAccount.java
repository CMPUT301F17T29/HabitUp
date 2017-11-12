package com.example.habitup.Model;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import com.example.habitup.Controller.ElasticSearchController;

/**
 * @author gojeffcho
 *
 * The system representation of a User.
 */
public class UserAccount {

    // Static Members
    private final static int xpIncrease = 25;

    // Members
    private int uid;
    private String username;
    private String realname;
    private Bitmap photo;
    private int level;
    private int XP;
    private int XPtoNext;

    /**
     * UserAccount Constructor
     * @param username: String representing username.  Must be unique.
     * @param realname: String representing real name.
     * @param photo: Image object, if provided
     *
     * @author @gojeffcho
     */
    public UserAccount(String username, String realname, Bitmap photo) throws
            IllegalArgumentException {

        this.setUniqueUID();
        this.setUsername(username);
        this.setRealname(realname);
        this.setPhoto(photo);

        level = 1;
        XP = 0;
        XPtoNext = 20;

    }

    /**
     * Gets the UID
     * @return int uid
     */
    public int getUID() { return this.uid; }

    /**
     * Gets unique String of UserAccount's username
     * @return String
     */
    public String getUsername() { return this.username; }

    /**
     * Gets String of UserAccount's real name
     * @return String
     */
    public String getRealname() { return this.realname; }

    /**
     * Gets Image if one is associated to the account, otherwise null
     * @return Image if associated, null if not
     */
    public Bitmap getPhoto() { return this.photo; }

    /**
     * Gets Attribute object owned by UserAccount
     * @return Attributes
     */
    public Attributes getAttributes() {
        // TODO: IMPLEMENT

        // ElasticSearch on uid to get Attributes object

        // Return Attributes object

        return new Attributes(this.uid); // TODO REMOVE
    }

    /**
     * Gets UserAccount's current level
     * @return Int
     */
    public int getLevel() { return this.level; }

    /**
     * Gets current XP of UserAccount
     * @return Int
     */
    public int getXP() { return this.XP; }

    /**
     * Gets next level-up XP target
     * @return Int
     */
    public int getXPtoNext() { return this.XPtoNext; }

    /**
     * Gets approved friends of UserAccount
     * @return ArrayList<UserAccount>
     */
    public UserAccountList getFriendsList() {

        // TODO: IMPLEMENT

        // ES on uid to get FriendsList

        // Return FriendsList

        return new UserAccountList(); // TODO REMOVE
    }

    /**
     * Returns current unapproved friend requests to UserAccount
     * @return ArrayList<UserAccount>
     */
    public UserAccountList getRequestsPending() {
        // TODO: IMPLEMENT

        // ES on uid to get RequestsPending

        // Return RequestsPending

        return new UserAccountList(); // TODO REMOVE
    }

    /**
     * Gets Habits defined by the UserAccount
     * @return ArrayList<Habit>
     */
    public HabitList getHabits() {
        // TODO: IMPLEMENT

        // ES on uid to get Habits

        // Add into a HabitList

        // Return HabitList

        return new HabitList(this.uid);  // TODO REMOVE
    }


    /**
     * Get the next unique UID, then set it to this user
     */
    public void setUniqueUID() {
        // ElasticSearch query: highest UID in use
        int newUID = -1;
        ElasticSearchController.GetMaxUidTask getMaxUID = new ElasticSearchController.GetMaxUidTask();
        getMaxUID.execute();
        try {
            newUID = getMaxUID.get();
            Log.i("HabitUpDEBUG", "UserAccount - UID was set to " + String.valueOf(newUID));
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "UserAccount - could not get Max UID");
        }

        // Set it to this user's uid
        this.uid = newUID;
    }

    /**
     * Method to update username
     * @param username
     * @throws IllegalArgumentException
     */
    public void setUsername(String username) throws IllegalArgumentException {

        // Catch invalid real names
        if (username.length() == 0) {
            throw new IllegalArgumentException("Error: username is blank.");
        } else if (username.length() > 15) {
            throw new IllegalArgumentException("Error: username must be 15 characters or fewer.");

        // Otherwise, legal: set the name
        } else {
            this.username = username;
        }
    }

    /**
     * Method to update realname
     * @param realname
     * @throws IllegalArgumentException
     */
    public void setRealname(String realname) throws IllegalArgumentException {

        // Catch invalid real names
        if (realname.length() == 0) {
            throw new IllegalArgumentException("Error: full name is blank.");

        } else if (realname.length() > 20) {
            throw new IllegalArgumentException("Error: full name must be 20 characters or fewer.");
        // Otherwise, legal: set the name
        } else {
            this.realname = realname;
        }
    }

    /**
     * Set or update UserAccount photo
     * @param photo
     */
    public void setPhoto(Bitmap photo) {
        if (photo != null) {
            this.photo = photo;
        }
    }

    /**
     * Delete the associated photo, if one exists
     */
    public void deletePhoto() {
       if (photo != null) {
           this.photo = null;
       }
    }

    /**
     * Set level to the next level, does boundary checking
     */
    public void incrementLevel() {
        this.level++;
    }

    /**
     * Increase UserAccount's current XP by xpAmount
     * @param xpAmount
     */
    public void increaseXP(int xpAmount) {
        this.XP += xpAmount;
    }

    /**
     * Set XP target to the next level-up target
     */
    public void setXPtoNext() {
        this.XPtoNext += xpIncrease;
    }

    /**
     * Add a friend request from the requestingUser
     * @param requestingUser
     * @return -1 if already in list; 0 if successfully added
     */
    public int addRequest(UserAccount requestingUser) {

        // ES on uid to get RequestsPending

        // Add requestingUser to RequestsPending

        // Put to ES

        return 0;
    }

    /**
     * Approve an existing request in requestsPending
     * @param requestingUser
     * @return -1 if unsuccessful,
     */
    public int approveRequest(UserAccount requestingUser) {

        // TODO: rework this whole thing
//        if (requestsPending.contains(requestingUser)) {
//            requestsPending.remove(requestingUser);
//            return friendsList.add(requestingUser);
//        } else {
//            return -1;
//        }
        return 0;

    }

    /**
     * Delete an existing Habit associated with the UserAccount
     * @param habit
     * @return 0 if successful, -1 if not in list
     */
    public int deleteHabit(Habit habit) {

        // TODO: rework this whole thing

//        if (habits.contains(habit)) {
//            habits.remove(habit);
//            return 0;
//        } else {
//            return -1;
//        }
        return 0;
    }
}
