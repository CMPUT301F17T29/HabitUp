package com.example.habitup;

import com.example.habitup.Model.Habit;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Ty on 2017-11-12.
 */

public class HabitTest {

    //Test Habit construction error long name
    @Test
    public void testHabitConstLongNameErr() {
        String name = "123456789abcdefjhijklmnop";

        try{
            Habit testHabit = new Habit(1);
            testHabit.setHabitName(name);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
            return;
        }
        assertTrue(Boolean.FALSE); //failed to reproduce the error
    }

    //Test Habit construction error long reason
    @Test
    public void testHabitConstLongReasonErr(){
        String reason = "123456789123456789123456789101010";

        try{
            Habit testHabit = new Habit(1);
            testHabit.setReason(reason);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
            return;
        }
        assertTrue(Boolean.FALSE); //failed to reproduce the error
    }

    //Test Habit construction error no schedule
    @Test
    public void testHabitConstSchedErr(){
        boolean[] schedule = new boolean[8];

        try{
            Habit testHabit = new Habit(1);
            testHabit.setSchedule(schedule);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
            return;
        }
        assertTrue(Boolean.FALSE); //failed to reproduce the error
    }

    //Test setHabitName actually sets/replaces the name
    @Test
    public void testSetHabitNameSuccess() {
        String habitName = "sampleHabit";

        Habit testHabit = new Habit(1);

        testHabit.setHabitName(habitName);

        assertTrue(testHabit.getHabitName() == habitName); //check for match
    }

    //Tests setHabitName to not exceed 20 characters, expects an error
    @Test
    public void testHabitNameLength() {
        String habitName = "123456789abcdefghijklmnop";
        Habit testHabit = new Habit(1);

        try{
            testHabit.setHabitName(habitName);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
            return;
        }

        assertTrue(Boolean.FALSE); //failed to replicate the error
    }

    //Tests that setReason sets
    @Test
    public void testSetReason() {
        String reason2 = "I wanna be the very best";
        Habit testHabit = new Habit(1);

        testHabit.setReason(reason2);

        assertTrue(testHabit.getHabitReason() == reason2); //check if habit reason matches
    }

    //Tests setReason to not exceed 30 characters, expecting an error
    @Test
    public void testSetReasonLength() {
        String reason2 = "123456789123456789123456789101010";
        Habit testHabit = new Habit(1);

        try{
            testHabit.setReason(reason2);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
            return;
        }
        assertTrue(Boolean.FALSE); //failed to replicate error
    }

    //Tests setSchedule sets
    @Test
    public void testSetSchedule(){
        boolean[] schedule2 = {false,true, false};
        Habit testHabit = new Habit(1);

        testHabit.setSchedule(schedule2);

        assertTrue(testHabit.getHabitSchedule()== schedule2); //check if schedule changed
    }

    //Test setAttribute actually sets an attribute
    @Test
    public void testSetAttribute(){
        String attribute = "Mental";
        String attribute2 = "Physical";
        Habit testHabit = new Habit(1);
        testHabit.setAttribute(attribute);
        testHabit.setAttribute(attribute2);
        assertTrue(testHabit.getHabitAttribute()== attribute2); //check attribute match
    }

}
