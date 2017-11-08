package com.example.habitup.Model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.habitup.Controller.HabitUpController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

// NOTE: @gojeffcho changed GregorianCalendar to LocalDate, added Comparator for sorting on date

/**
 * @author acysl
 */
public class HabitEvent implements Comparable<HabitEvent> {

    private int uid;
    private Habit habit;
    private String comment;
    private LocalDate completedate;
    private Bitmap Image;
    private Map location;

    private String pathofimage;


    public HabitEvent () {
//        this.comment = comment;
//        this.completedate = completedate;
//        this.location = location;
//        this.comment = comment;
//        this.pathofimage = image;
//        this.habit = habit;
    }

    public HabitEvent(int uid, Habit habit, String comment, LocalDate completedate) {
        this.setUID(uid);
        this.setHabit(habit);
        this.setComment(comment);
        this.setCompletedate(completedate);
    }
  
    // Copy Constructor - @gojeffcho
    public HabitEvent(HabitEvent e) {
        // Copy all members over
        this.setUID(e.getUID());
        this.setHabit(getHabit());
        this.setComment(e.getComment());
        this.setCompletedate(e.getCompletedate());
        this.setLocation(e.getLocation());
        this.setImage(e.getImage());
//        this.pathofimage = ??

    }

    public void setUID(int uid) { this.uid = uid; }

    public void setHabit(Habit h) { this.habit = h; }

    public void setComment (String comment) throws IllegalArgumentException {
        this.comment = comment;
    }

    public void setCompletedate (LocalDate completedate) throws IllegalArgumentException {
        this.completedate = completedate;

    }
    public void setLocation(Map location){
        this.location =  location;
    }

    public void setPathofimage(String pathofimage){
        this.pathofimage = pathofimage;
        setImage(pathofimage);

    }

    private void setImage(String pathOfImage){
        Image= BitmapFactory.decodeFile(pathOfImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        while(true){
            if (stream.toByteArray().length >= 65536) {
                resizeImage(Image);
            }
            else{
                break;
            }
        }
    }

    // TODO: IMPLEMENT
    private void setImage(Bitmap image) {

    }

    private void resizeImage(Bitmap bp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
    }


    public int getUID() { return this.uid; }

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
        return Image;
    }

    public Habit getHabit() { return habit; }

    public int compareTo(HabitEvent e) {
        return this.completedate.compareTo(e.getCompletedate());
    }

}
