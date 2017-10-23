package com.example.habitup.Model;

import android.media.Image;

import java.util.ArrayList;

/**
 *
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
    private ArrayList<UserAccount> friendsList;
    private ArrayList<UserAccount> requestsPending;
    private ArrayList<Habit> habits;

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
        friendsList = new ArrayList<>();
        requestsPending = new ArrayList<>();
        habits = new ArrayList<>();
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
    public ArrayList<UserAccount> getFriendsList() { return this.friendsList; }

    /**
     * Returns current unapproved friend requests to UserAccount
     * @return ArrayList<UserAccount>
     */
    public ArrayList<UserAccount> getRequestsPending() { return this.requestsPending; }

    /**
     * Gets Habits defined by the UserAccount
     * @return ArrayList<Habit>
     */
    public ArrayList<Habit> getHabits() { return this.habits; }

    /**
     * Method to update realname
     * @param realname
     * @throws IllegalArgumentException
     */
    public void setRealname(String realname) throws IllegalArgumentException {
        // TODO: IMPLEMENT
    }

    /**
     * Set or update UserAccount photo
     * @param photo
     */
    public void setPhoto(Image photo) {
        // TODO: IMPLEMENT
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
     */
    public void addRequest(UserAccount requestingUser) {
        // TODO: IMPLEMENT
    }

    /**
     * Approve an existing request in requestsPending
     * @param requestingUser
     */
    public void approveRequest(UserAccount requestingUser) {
        // TODO: IMPLEMENT
    }

    /**
     * Add a new Habit and associated it with the UserAccount
     * @param habit
     */
    public void addHabit(Habit habit) {
        // TODO: IMPLEMENT
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
     */
    public void deleteHabit(Habit habit) {
        // TODO: IMPLEMENT
    }
}
