package com.example.habitup.Model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;

import com.example.habitup.Controller.HabitUpApplication;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

/**
 * HabitEvent represents an instance of a Habit being completed.  A HabitEvent should only be added
 * when the event has been done; therefore, it can only ever be on the current day or before.  The
 * HabitEvent is associated to a UserAccount (uid) and a Habit belonging to that user (hid), and is
 * uniquely identified by an EventID (eid).  It has a completedate and optionally a comment, photo,
 * and/or location.  It also stores whether or not it was completed on a scheduled or unscheduled
 * date, for stats calculation and update purposes, e.g. on edit or delete.
 *
 * @author acysl
 * @author gojeffcho (updates)
 *
 * Javadoc last updated 2017-11-13 by @gojeffcho
 */
public class HabitEvent implements Comparable<HabitEvent> {

    private int uid;
    private int hid;
    private String eid;
    private String comment;
    private LocalDate completedate;
    private Bitmap photo;
    private String encodedPhoto;
    private Location location;
    private Boolean scheduled;

    private String habitName = "";
    private String habitAttribute = "";

    /**
     * Constructor - needs a uid and hid to uniquely identify it.  eid is set upon transmission to
     * ElasticSearch.
     * @param uid int (UserAccount id)
     * @param hid int (Habit id)
     */
    public HabitEvent (int uid, int hid) {
        this.uid = uid;
        this.hid = hid;
    }

    /**
     * Copy Constructor - required for some container operations
     * @param e HabitEvent (object to copy fields from)
     */
    public HabitEvent(HabitEvent e) {

        // Copy all members over
        this.setUID(e.getUID());
        this.setHabit(e.getHID());
        this.setEID(e.getEID());
        this.setComment(e.getComment());
        this.setCompletedate(e.getCompletedate());
        this.setPhoto(e.getPhoto());
        this.setLocation(e.getLocation());
        this.scheduled = e.getScheduled();
    }

    /**
     * Set the UID of the HabitEvent
     * @param uid int (UserAccount id)
     */
    public void setUID(int uid) { this.uid = uid; }

    /**
     * Set the Habit the HabitEvent belongs to
     * @param hid int (Habit id)
     */
    public void setHabit(int hid) {

        this.hid = hid;
    }

    /**
     * Set the unique HabitEvent id
     * @param uuid String (UUID from ElasticSearch)
     */
    public void setEID(String uuid) { this.eid = uuid; }

    /**
     * TODO: IMPLEMENT
     *
     * Sets whether or not this HabitEvent was completed on a scheduled date.  Used in stats
     * calculations.
     */
    public void setScheduled() {

        // Get Habit corresponding to hid from ES

        // Get schedule from Habit

        // Check if completedate is a scheduled date

            // If yes, set TRUE

            // If not, set FALSE
    }

    /**
     * Validate and set HabitEvent comment
     * @param comment String (comment)
     * @throws IllegalArgumentException (if comment not legal)
     */
    public void setComment (String comment) throws IllegalArgumentException {
        if (comment.length() <= 20) {
            this.comment = comment;
        } else {
            throw new IllegalArgumentException("Comment must be between 0 and 20 characters.");
        }
    }

    /**
     * Validate and set completedate
     * @param completedate LocalDate (date of completion)
     * @throws IllegalArgumentException (if date not legal - must be current date or before)
     */
    public void setCompletedate (LocalDate completedate) throws IllegalArgumentException {
        if (completedate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Completion date must be on or before today's date.");
        } else {
            this.completedate = completedate;
        }
    }

    /**
     * Sets the location of the event
     * @param location Location (location of event)
     */
    public void setLocation(Location location){
        this.location =  location;
    }

    /**
     * Set or update UserAccount photo
     * @param photo Bitmap (photo to set for event)
     */
    public void setPhoto(Bitmap photo) {

        if (photo != null) {

//            Log.i("HabitUpDEBUG", "Photo is " + String.valueOf(photo.getByteCount()) + " bytes.");

            if (photo.getByteCount() > HabitUpApplication.MAX_PHOTO_BYTECOUNT) {
                for (int i = 0; i < 3; ++i) {
                    photo = resizeImage(photo);
//                    Log.i("HabitUpDEBUG", "Resized to " + String.valueOf(photo.getByteCount()) + " bytes.");
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
     * If the photo did not meet the size constraint, try to reduce the size by scaling it down
     * @param img Bitmap (Photo to resize)
     * @return Bitmap (resized photo)
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
     * Set photo and encodedphoto to null, clearing them
     */
    public void deletePhoto() {
        this.photo = null;
        this.encodedPhoto = null;
    }

    /**
     * Get the UID associated with this HabitEvent
     * @return int (User ID)
     */
    public int getUID() { return this.uid; }

    /**
     * Get the HID associated with this HabitEvent
     * @return int (Habit ID)
     */
    public int getHID() { return hid; }

    /**
     * Get the unique HabitEvent identifier
     * @return String (HabitEvent ID)
     */
    public String getEID() { return eid; }

    /**
     * Get whether the HabitEvent was completed on a scheduled date.
     * @return Boolean (True if HabitEvent was completed on a scheduled date)
     */
    public Boolean getScheduled() { return this.scheduled; }

    /**
     * Get the date the HabitEvent was completed.
     * @return LocalDate (HabitEvent completion date)
     */
    public LocalDate getCompletedate(){
        return completedate;
    }

    /**
     * Get the location of the HabitEvent
     * @return Location (Location of HabitEvent)
     */
    public Location getLocation(){
        return location;
    }

    /**
     * Get the comment for the HabitEvent
     * @return String (Comment)
     */
    public String getComment(){
        return comment;
    }

    /**
     * Decode the photo, if it has not already been decoded, and return the image file
     * @return Bitmap (HabitEvent Photo)
     */
    public Bitmap getPhoto() {
        decodePhoto();
        return photo;
    }

    /**
     * Decodes the encodedPhoto and sets the photo Bitmap to it.
     */
    private void decodePhoto() {
        if (this.encodedPhoto != null) {
            byte [] decodedBytes = Base64.decode(this.encodedPhoto, 0);
            this.photo = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }

    /**
     * Comparator implementation - sort in reverse chronological order
     * @param e HabitEvent (to compare to)
     * @return int (comparison code)
     */
    public int compareTo(HabitEvent e) {
        return (-1) * this.completedate.compareTo(e.getCompletedate());
    }

    /**
     * Checks whether the HabitEvent has an associated photo.
     * @return boolean (True if the HabitEvent has a photo)
     */
    public boolean hasImage() {
        return this.photo != null;
    }

    /**
     * Checks whether the HabitEvent has an assocaited location.
     * @return boolean (True if the HabitEvent has a location)
     */
    public boolean hasLocation() {
        return this.location != null;
    }

    public void setHabitStrings(Habit habit) {
        this.habitName = habit.getHabitName();
        this.habitAttribute = habit.getHabitAttribute();
    }

    /**
     * Get the event's habit name
     * @return the name of the habit the event belongs to
     */
    public String getHabitName() {
        return this.habitName;
    }

    /**
     * Get the event's habit attribute
     * @return the name of the attribute the event's habit belongs to
     */
    public String getHabitAttribute() {
        return this.habitAttribute;
    }

}
