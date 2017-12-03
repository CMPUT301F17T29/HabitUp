package com.example.habitup.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * @author gojeffcho
 *
 * The system representation of a User.  Stores a unique UID identifying the user, a username that
 * is used for login/friend requests, a real name that is displayed in the app, and optionally a
 * photo as a display picture.  encodedPhoto is what we use to be able to store images to
 * ElasticSearch by transcoding them to Strings.  We will likely separate these out into their own
 * wrapper class for P5.
 *
 * Javadoc last updated 2017-11-13 by @gojeffcho.
 */
public class UserAccount {

    // Members
    private int uid;
    private String username;
    private String realname;
    private String encodedPhoto;
    private Bitmap photo;
    private int level;
    private int XP;
    private int XPtoNext;

    private HabitEventList eventList;
    private HabitList habitList;
    private UserAccountList requestList;
    private UserAccountList friendsList;

    /**
     * Empty constructor for GSON
     */
    public UserAccount() {

    }

    /**
     * Constructor for a UserAccount.
     *
     * @param username String (max 15 chars)
     * @param realname String (max 25 chars)
     * @param photo Bitmap
     * @throws IllegalArgumentException if constraints are not met
     */
    public UserAccount(String username, String realname, Bitmap photo) throws IllegalArgumentException {
        this.setUniqueUID();
        this.setUsername(username);
        this.setRealname(realname);
        this.setPhoto(photo);

        // RPG baseline stats
        level = 1;
        XP = 0;
        XPtoNext = 20;

        this.eventList = new HabitEventList();
        this.habitList = new HabitList();
        this.friendsList = new UserAccountList();
        this.requestList = new UserAccountList();
    }

    /**
     * Gets the UID
     * @return int UID
     */
    public int getUID() { return this.uid; }

    /**
     * Gets unique String of UserAccount's username
     * @return String username
     */
    public String getUsername() { return this.username; }

    /**
     * Gets String of UserAccount's real name
     * @return String realname
     */
    public String getRealname() { return this.realname; }

    /**
     * Gets Image if one is associated to the account, otherwise null
     * @return Bitmap if associated, null if not
     */
    public Bitmap getPhoto() {
        decodePhoto();
        return photo;
    }

    /**
     * Decodes the encodedPhoto if it exists and sets the photo Bitmap to it.
     */
    public void decodePhoto() {
        if (this.encodedPhoto != null) {
            byte [] decodedBytes = Base64.decode(this.encodedPhoto, 0);
            this.photo = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }

    /**
     * Gets UserAccount's current level
     * @return int currentLevel
     */
    public int getLevel() { return this.level; }

    /**
     * Gets current XP of UserAccount
     * @return int Current XP
     */
    public int getXP() { return this.XP; }

    /**
     * Gets next level-up XP target
     * @return int Next XP target level
     */
    public int getXPtoNext() { return this.XPtoNext; }

    /**
     * Get the next unique UID from ElasticSearch, then set it to this user's UID
     */
    public void setUniqueUID() {
        // ElasticSearch query: highest UID in use
        int newUID = -1;

        ElasticSearchController.GetMaxUidTask getMaxUID = new ElasticSearchController.GetMaxUidTask();
        getMaxUID.execute();
        try {
            newUID = getMaxUID.get().intValue();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "UserAccount - could not get Max UID");
        }

        // Set it to this user's uid
        this.uid = newUID;
    }

    /**
     * Validate and update username
     * @param username String
     * @throws IllegalArgumentException if username not legal
     */
    public void setUsername(String username) throws IllegalArgumentException {

        // Catch invalid real names
        if (username.length() == 0) {
            throw new IllegalArgumentException("Error: username is blank.");
        } else if (username.length() > HabitUpApplication.MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Error: username must be " +
                    String.valueOf(HabitUpApplication.MAX_USERNAME_LENGTH) + " characters or fewer.");

        // Otherwise, legal: set the name
        } else {
            this.username = username;
        }
    }

    /**
     * Validate and update realname
     * @param realname String
     * @throws IllegalArgumentException if realname not legal
     */
    public void setRealname(String realname) throws IllegalArgumentException {

        // Catch invalid real names
        if (realname.length() == 0) {
            throw new IllegalArgumentException("Error: full name is blank.");

        } else if (realname.length() > HabitUpApplication.MAX_REALNAME_LENGTH) {
            throw new IllegalArgumentException("Error: full name must be " +
                    String.valueOf(HabitUpApplication.MAX_REALNAME_LENGTH) + " characters or fewer.");
        // Otherwise, legal: set the name
        } else {
            this.realname = realname;
        }
    }

    /**
     * Set or update UserAccount photo.  If photo is over the size limit, it attemps three times to
     * shrink the filesize by scaling the photo down.
     * @param photo Bitmap
     */
    public void setPhoto(Bitmap photo) {

        if (photo != null) {

            Log.i("HabitUpDEBUG", "Photo is " + String.valueOf(photo.getByteCount()) + " bytes.");

            if (photo.getByteCount() > HabitUpApplication.MAX_PHOTO_BYTECOUNT) {
                for (int i = 0; i < 3; ++i) {
                    photo = resizeImage(photo);
                    Log.i("HabitUpDEBUG", "Resized to " + String.valueOf(photo.getByteCount()) + " bytes.");
                    if (photo.getByteCount() <= HabitUpApplication.MAX_PHOTO_BYTECOUNT) {
                        break;
                    }
                }
            }

            if (photo.getByteCount() <= HabitUpApplication.MAX_PHOTO_BYTECOUNT) {

                this.photo = photo;
                ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
                this.encodedPhoto = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

            } else {
                throw new IllegalArgumentException("Image file must be less than or equal to " +
                        String.valueOf(HabitUpApplication.MAX_PHOTO_BYTECOUNT) + " bytes.");
            }

        } else {
            this.photo = null;
            this.encodedPhoto = null;
        }
    }

    /**
     * Helper method - tries to resize the image by a scaling factor.
     * @param img Bitmap
     * @return Bitmap after resizing
     */
    private Bitmap resizeImage(Bitmap img) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        img.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//        byte[] imgData = stream.toByteArray();
//        return BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
        double scaleFactor = 0.95;
        return Bitmap.createScaledBitmap(img, (int) (img.getWidth() * scaleFactor), (int) (img.getHeight() * scaleFactor), true);
    }

    /**
     * Delete the associated photo, if one exists, by setting photo and encodedPhoto to null.
     */
    public void deletePhoto() {
       encodedPhoto = null;
       photo = null;
    }

    /**
     * Set level to the next level.
     * TODO: boundary checking for MAX_INT
     */
    public void incrementLevel() {
        if(this.level < Integer.MAX_VALUE) {
            this.level++;
        }
    }

    /**
     * Increase UserAccount's current XP by xpAmount
     * @param xpAmount int (amount to increase XP by)
     */
    public void increaseXP(int xpAmount) {
        if ((this.XP - xpAmount) < Integer.MAX_VALUE -xpAmount){
            this.XP += xpAmount;
        }
        else this.XP = Integer.MAX_VALUE;
    }

    /**
     * Set XP target to the next level-up target
     */
    public void setXPtoNext() {
        this.XPtoNext += HabitUpApplication.XP_INCREASE_AMOUNT;
    }

    /**
     * Return the user's list of habits
     * @return the HabitList model
     */
    public HabitList getHabitList() {
        return this.habitList;
    }

    /**
     * Return the user's list of habit events
     * @return the HabitEventList model
     */
    public HabitEventList getEventList() {
        return this.eventList;
    }

    /**
     * Get the user's list of follow requests
     * @return the UserAccountList array of request user accounts
     */
    public UserAccountList getRequestList() {
        return this.requestList;
    }

    /**
     * Get the user's list of friends
     * @return the UserAccountList array of friend user accounts
     */
    public UserAccountList getFriendsList() {
        return this.friendsList;
    }

    /**
     * When two user accounts are compared, they should be equal if they have the same UIDs
     * @param obj the other user account to compare with
     * @return true if the two user accounts have the same UID
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        final UserAccount other = (UserAccount) obj;

        return this.getUsername() == other.getUsername();
    }

    public void setDemoPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
