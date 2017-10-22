package com.example.habitup.Model;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.GregorianCalendar;


public class HabitEvent {
    private GregorianCalendar completedate;
    private Map location;
    private Bitmap Image;
    private String comment;
    private byte[] ImageByteArray;
    private int ByteCount;

    public void setCompletedate(GregorianCalendar completedate){
        this.completedate = completedate;

    }
    public void setLocation(Map location){
        this.location =  location;
    }
    public void setImage(String pathOfImage){
        Image= BitmapFactory.decodeFile(pathOfImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        while(true){
            ImageByteArray = stream.toByteArray();
            ByteCount = ImageByteArray.length;
            if (ByteCount >= 65536) {
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

    public void setComment(String comment){
        this.comment = comment;
    }

    public GregorianCalendar getCompletedate(){
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


}
