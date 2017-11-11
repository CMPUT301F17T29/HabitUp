package com.example.habitup.Model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

// NOTE: @gojeffcho changed GregorianCalendar to LocalDate, added Comparator for sorting on date

/**
 * @author acysl
 */
public class HabitEvent implements Comparable<HabitEvent> {

    private static final int maxByteCount = 65536;

    private int uid;
    private int hid;
    private String eid;
    private String comment;
    private LocalDate completedate;
    private Bitmap image;
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
        this.setImage(e.getImage());
        this.setLocation(e.getLocation());
        this.scheduled = e.getScheduled();
//        this.pathofimage = ??

    }

    public void setUID(int uid) { this.uid = uid; }

    public void setHabit(int hid) { this.hid = hid; }

    public void setEid(String uuid) { this.eid = uuid; }

    public void setImage(Bitmap image) throws IllegalArgumentException {
        if (image.getByteCount() <= maxByteCount) {
            this.image = image;
        } else {
            throw new IllegalArgumentException("Image file must be less than or equal to " + String.valueOf(maxByteCount) + " bytes.");
        }
    }

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

    public void setPathofimage(String pathofimage){
        this.pathofimage = pathofimage;
        setImage(pathofimage);

    }

    private void setImage(String pathOfImage){
        image = BitmapFactory.decodeFile(pathOfImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        while(true){
            if (stream.toByteArray().length >= maxByteCount) {
                resizeImage(image);
            }
            else{
                break;
            }
        }
    }

    private void resizeImage(Bitmap bp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
    }


    public int getUID() { return this.uid; }

    public int getHID() { return hid; }

    public String getEid() { return this.eid; }

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

    public Bitmap getImage(){
        return image;
    }

    public int compareTo(HabitEvent e) {
        return this.completedate.compareTo(e.getCompletedate());
    }

    public boolean hasImage() {
        return this.image != null;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

}
