package com.example.habitup.Model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.habitup.Controller.HabitUpApplication;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

/**
 * @author acysl
 */
public class HabitEvent implements Comparable<HabitEvent> {

    private int uid;
    private int hid;
    private String eid;
    private String comment;
    private LocalDate completedate;
    private Bitmap photo;
    private String encodedPhoto;
    private Map location;
    private Boolean scheduled;

    private String pathofimage;


    public HabitEvent (int uid, int hid) {
        this.uid = uid;
        this.hid = hid;
//        this.comment = comment;
//        this.completedate = completedate;
//        this.location = location;
//        this.comment = comment;
//        this.pathofimage = image;
//        this.habit = habit;
    }

    public HabitEvent(int uid, int hid, String comment, LocalDate completedate) {
        this.setUID(uid);
        this.setHabit(hid);
        this.setComment(comment);
        this.setCompletedate(completedate);
        this.setScheduled();
    }

    // Copy Constructor - @gojeffcho
    public HabitEvent(HabitEvent e) {
        // Copy all members over
        this.setUID(e.getUID());
        this.setHabit(e.getHID());
        this.setComment(e.getComment());
        this.setCompletedate(e.getCompletedate());
        this.setPhoto(e.getPhoto());
        this.setLocation(e.getLocation());
        this.scheduled = e.getScheduled();
//        this.pathofimage = ??

    }

    public void setUID(int uid) { this.uid = uid; }

    public void setHabit(int hid) { this.hid = hid; }

    public void setEID(String uuid) { this.eid = uuid; }

    public void setScheduled() {

        // Get Habit corresponding to hid from ES

        // Get schedule from Habit

        // Check if completedate is a scheduled date

            // If yes, set TRUE

            // If not, set FALSE

    }

    public void setComment (String comment) throws IllegalArgumentException {
        if (comment.length() >= 0 || comment.length() <= 20) {
            this.comment = comment;
        } else {
            throw new IllegalArgumentException("Comment must be between 0 and 20 characters.");
        }
    }

    public void setCompletedate (LocalDate completedate) throws IllegalArgumentException {
        if (completedate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Completion date must be on or before today's date.");
        } else {
            this.completedate = completedate;
        }
    }

    public void setLocation(Map location){
        this.location =  location;
    }

    /**
     * Set or update UserAccount photo
     * @param photo
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

    private Bitmap resizeImage(Bitmap img) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        img.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//        byte[] imgData = stream.toByteArray();
//        return BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
        double scaleFactor = 0.95;
        return Bitmap.createScaledBitmap(img, (int) (img.getWidth() * scaleFactor), (int) (img.getHeight() * scaleFactor), true);
    }

    public void deletePhoto() {
        this.photo = null;
        this.encodedPhoto = null;
    }

    public int getUID() { return this.uid; }

    public int getHID() { return hid; }

    public String getEID() { return eid; }

    public Boolean getScheduled() { return this.scheduled; }

    public LocalDate getCompletedate(){
        return completedate;
    }

    public Map getLocation(){
        return location;
    }

    public String getComment(){
        return comment;
    }

    public Bitmap getPhoto() {
        decodePhoto();
        return photo;
    }

    /**
     * Decodes the encodedPhoto and sets the photo Bitmap to it.
     */
    public void decodePhoto() {
        if (this.encodedPhoto != null) {
            byte [] decodedBytes = Base64.decode(this.encodedPhoto, 0);
            this.photo = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }

    public int compareTo(HabitEvent e) {
        return (-1) * this.completedate.compareTo(e.getCompletedate());
    }

    public boolean hasImage() {
        return this.photo != null;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

}
