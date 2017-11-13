package com.example.habitup;


import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import com.example.habitup.Model.HabitEvent;

import org.junit.Test;

import java.time.LocalDate;

/**
 * Update by Shuyang on 2017-11-12.
 */

public class HabitEventUnitTest extends ActivityInstrumentationTestCase2 {

    public HabitEventUnitTest(){
        super(HabitEvent.class);
    }
    @Override
    public void setUp() throws Exception{

    }

    @Override
    public void tearDown() throws Exception {

    }

    @Override
    public void runTest() throws Exception {

    }

    @Test
    public void testHabitEventSetterGetter(){
        HabitEvent event1 = new HabitEvent(1,1);
        event1.setCompletedate(LocalDate.of(2017,12,12));
        Bitmap bm1 = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        event1.setImage(bm1);
        event1.setComment("Comment test for Habit Event");
        try {
            LocalDate date1 = LocalDate.of(2017,12,12);
            assertTrue((event1.getCompletedate()).compareTo(date1) == 0);
            assertTrue(event1.getImage() != null);
            assertTrue(event1.getComment().equals("Comment test for Habit Event"));
        } catch (Exception e) {
            assertTrue(Boolean.FALSE);
            return;
        }
        assertTrue(Boolean.TRUE);
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
        LocalDate date1 = LocalDate.of(2014,12,12);
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
        Bitmap bm2 = Bitmap.createBitmap(666666, 666666, Bitmap.Config.ARGB_8888);
        HabitEvent event1 = new HabitEvent(1,1);
        try {
            event1.setImage(bm2);
        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
            return;
        }

        assertTrue(Boolean.FALSE);
    }

}