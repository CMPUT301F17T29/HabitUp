package com.example.habitup.Model;

import android.media.Image;

import java.util.ArrayList;

/**
 * @author gojeffcho
 *
 * The system representation of a User.
 */
public class UserAccount {

    // Static Members
    private final static int xpIncrease = 25;

    // Members
    private String username;
    private String realname;
    private Image photo;
    private Attributes attributes;
    private int level;
    private int XP;
    private int XPtoNext;
    private UserAccountList friendsList;
    private UserAccountList requestsPending;
    private HabitList habits;

    /**
     * UserAccount Constructor
     * @param username: String representing username.  Must be unique.
     * @param realname: String representing real name.
     * @param photo: Image object, if provided
     *
     * @author @gojeffcho
     */
    public UserAccount(String username, String realname, Image photo) throws
            IllegalArgumentException, IllegalStateException {
        this.username = username;
        this.realname = realname;
        this.photo = photo;
        attributes = new Attributes();
        level = 1;
        XP = 0;
        XPtoNext = 20;
        friendsList = new UserAccountList();
        requestsPending = new UserAccountList();
        habits = new HabitList();
    }

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
    public Image getPhoto() { return this.photo; }

    /**
     * Gets Attribute object owned by UserAccount
     * @return Attributes
     */
    public Attributes getAttributes() { return this.attributes; }

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
    public UserAccountList getFriendsList() { return this.friendsList; }

    /**
     * Returns current unapproved friend requests to UserAccount
     * @return ArrayList<UserAccount>
     */
    public UserAccountList getRequestsPending() { return this.requestsPending; }

    /**
     * Gets Habits defined by the UserAccount
     * @return ArrayList<Habit>
     */
    public HabitList getHabits() { return this.habits; }

    /**
     * Method to update realname
     * @param realname
     * @throws IllegalArgumentException
     */
    public void setRealname(String realname) throws IllegalArgumentException {

        // Catch invalid real names
        if (realname.length() == 0) {
            throw new IllegalArgumentException();

        // Otherwise, legal: set the name
        } else {
            this.realname = realname;
        }
    }

    /**
     * Set or update UserAccount photo
     * @param photo
     */
    public void setPhoto(Image photo) {
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

        return requestsPending.add(requestingUser);
    }

    /**
     * Approve an existing request in requestsPending
     * @param requestingUser
     * @return -1 if unsuccessful,
     */
    public int approveRequest(UserAccount requestingUser) {
        if (requestsPending.contains(requestingUser)) {
            requestsPending.remove(requestingUser);
            return friendsList.add(requestingUser);
        } else {
            return -1;
        }

    }

    /**
     * Add a new Habit and associated it with the UserAccount
     * @param habit
     */
    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    /**
     * Edit an existing Habit associated with the UserAccount
     * @param habit
     */
    public void editHabit(Habit habit) {
        // TODO: IMPLEMENT
    }

    /**
     * Delete an existing Habit associated with the UserAccount
     * @param habit
     * @return 0 if successful, -1 if not in list
     */
    public int deleteHabit(Habit habit) {
        if (habits.contains(habit)) {
            habits.remove(habit);
            return 0;
        } else {
            return -1;
        }
    }
}
