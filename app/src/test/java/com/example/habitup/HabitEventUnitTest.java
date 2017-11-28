package com.example.habitup;


import android.graphics.Bitmap;


import com.example.habitup.Model.Habit;
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

//        assertTrue(Boolean.FALSE);
    }

    @Test
    public void testSetImage(){
        Bitmap bm1 = Bitmap.createBitmap(22, 22, Bitmap.Config.ARGB_8888);
        HabitEvent event1 = new HabitEvent(1,1);
        event1.setPhoto(bm1);
//        assertTrue(event1.hasImage());

    }

    @Test
    public void testEditHabitType() {
        String habitName1 = "Go to gym";
        String attribute1 = "Physical";
        String habitName2 = "Go to class";
        String attribute2 = "Discipline";

        Habit habit1 = new Habit(1);
        habit1.setHabitName(habitName1);
        habit1.setAttribute(attribute1);

        Habit habit2 = new Habit(2);
        habit2.setHabitName(habitName2);
        habit2.setAttribute(attribute2);

        HabitEvent event = new HabitEvent(1, habit1.getHID());
        event.setHabitStrings(habit1);
        assertTrue(event.getHabitName().equals(habitName1));
        assertTrue(event.getHabitAttribute().equals(attribute1));

        event.setHabit(2);
        event.setHabitStrings(habit2);
        assertTrue(event.getHabitName().equals(habitName2));
        assertTrue(event.getHabitAttribute().equals(attribute2));
    }

    @Test
    public void testEditHabitType() {
        String habitName1 = "Go to gym";
        String attribute1 = "Physical";
        String habitName2 = "Go to class";
        String attribute2 = "Discipline";

        Habit habit1 = new Habit(1);
        habit1.setHabitName(habitName1);
        habit1.setAttribute(attribute1);

        Habit habit2 = new Habit(2);
        habit2.setHabitName(habitName2);
        habit2.setAttribute(attribute2);

        HabitEvent event = new HabitEvent(1, habit1.getHID());
        event.setHabitStrings(habit1);
        assertTrue(event.getHabitName().equals(habitName1));
        assertTrue(event.getHabitAttribute().equals(attribute1));

        event.setHabit(2);
        event.setHabitStrings(habit2);
        assertTrue(event.getHabitName().equals(habitName2));
        assertTrue(event.getHabitAttribute().equals(attribute2));
    }

}
