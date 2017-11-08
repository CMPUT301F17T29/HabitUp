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
  
    private LocalDate completedate;
    private Map location;
    private Bitmap Image;
    private String pathofimage;
    private String comment;
    private Habit habit;


    public HabitEvent () {
//        this.comment = comment;
//        this.completedate = completedate;
//        this.location = location;
//        this.comment = comment;
//        this.pathofimage = image;
//        this.habit = habit;
    }
  
    // Copy Constructor - @gojeffcho
    public HabitEvent(HabitEvent e) {
        // Copy all members over
        this.completedate = e.getCompletedate();
        this.location = e.getLocation();
        this.Image = e.getImage();
//        this.pathofimage = ??
        this.comment = e.getComment();
        this.habit = e.getHabit();
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
    private void resizeImage(Bitmap bp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
    }

    public void setComment (String comment) throws IllegalArgumentException {
        this.comment = comment;
    }

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

<<<<<<< HEAD
    public Habit getHabit() { return this.habit;}
=======
    public Habit getHabit() { return habit; }

    public int compareTo(HabitEvent e) {
        return this.completedate.compareTo(e.getCompletedate());
    }
>>>>>>> origin/development

}
