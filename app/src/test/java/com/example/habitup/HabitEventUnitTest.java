package com.example.habitup;


import android.graphics.Bitmap;


import com.example.habitup.Model.HabitEvent;

import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.Assert.assertTrue;


public class HabitEventUnitTest {


    @Test
    public void testLocation(){
        assertTrue(true);

    }

    @Test
    public void testCommentLimited(){
        String c1 = "testcommentlimitedddddddddddddddddddddddddddd";
        HabitEvent event1 = new HabitEvent(1,1);
        try {
            event1.setComment(c1);

        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
            return;
        }

        assertTrue(Boolean.FALSE);
    }

    @Test
    public void testCompleteDateRestrict(){
        LocalDate date1 = LocalDate.of(2018,12,12);
        HabitEvent event1 = new HabitEvent(1,1);
        try {
            event1.setCompletedate(date1);
        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
            return;
        }

        assertTrue(Boolean.FALSE);
    }

    @Test
    public void testLargerImage(){
        Bitmap bm2 = Bitmap.createBitmap(999999, 999999, Bitmap.Config.ARGB_8888);
        HabitEvent event1 = new HabitEvent(1,1);
        try {
            event1.setPhoto(bm2);
        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
            return;
        }

        assertTrue(Boolean.FALSE);
    }

    @Test
    public void testSetImage(){
        Bitmap bm1 = Bitmap.createBitmap(22, 22, Bitmap.Config.ARGB_8888);
        HabitEvent event1 = new HabitEvent(1,1);
        event1.setPhoto(bm1);
        assertTrue(event1.hasImage());

    }

}
