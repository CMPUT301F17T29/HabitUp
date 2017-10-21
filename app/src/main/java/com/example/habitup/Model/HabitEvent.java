package com.example.habitup.Model;



import android.graphics.Bitmap;

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
    public void setImage(Picture Image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageByteArray = stream.toByteArray();
        ByteCount = ImageByteArray.length;
        if (ByteCount >= 65536){

        }else
            { }
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
