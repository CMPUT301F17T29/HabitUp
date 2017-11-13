package com.example.habitup;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;

import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Ty on 2017-11-12.
 */

public class HabitTest {

    //Test Habit construction error long name
    @Test
    public void testHabitConstLongNameErr() {
        String name = "123456789abcdefjhijklmnop";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        boolean[] schedule = {true};

        Habit habit = new Habit()

        try{
            Habit testHabit = new Habit(name,);
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
        String name = "name";
        String reason = "123456789123456789123456789101010";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};

        try{
            Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);
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
        String name = "name";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,
                Boolean.FALSE,Boolean.FALSE,Boolean.FALSE};

        try{
            Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);
        }
        catch (IllegalStateException err) {
            assertTrue(Boolean.TRUE);
            return;
        }
        assertTrue(Boolean.FALSE); //failed to reproduce the error
    }

    //Test setHabitName actually sets/replaces the name
    @Test
    public void testSetHabitNameSuccess() {
        String name = "name";
        String habitName = "sampleHabit";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

        testHabit.setHabitName(habitName);

        assertTrue(testHabit.getHabitName() == habitName); //check for match
    }

    //Tests that setHabitName does not accept the same name of an existing habit
    @Test //TODO maybe a controller test
    public void testSetHabitNameErr(){
        String name = "name";
        String habitName = "sampleHabit";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);
        Habit testHabit2 = new Habit(habitName,reason,attribute,startDate,schedule);

        try{
            testHabit2.setHabitName(name);
        }
        catch (IllegalStateException err) {
            assertTrue(Boolean.TRUE);
            return;
        }
        assertTrue(Boolean.FALSE); //failed to reproduce error (same as testHabit)
    }

    //Tests setHabitName to not exceed 20 characters, expects an error
    @Test
    public void testHabitNameLength() {
        String name = "name";
        String habitName = "123456789abcdefghijklmnop";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

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
        String name = "name";
        String reason = "reason";
        String reason2 = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

        testHabit.setReason(reason2);

        assertTrue(testHabit.getHabitReason() == reason2); //check if habit reason matches
    }

    //Tests setReason to not exceed 30 characters, expecting an error
    @Test
    public void testSetReasonLength() {
        String name = "name";
        String habitName = "sampleHabit";
        String reason = "I wanna be the very best";
        String reason2 = "123456789123456789123456789101010";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

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
        String name = "name";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Boolean[] schedule2 = {Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,
                Boolean.FALSE,Boolean.TRUE,Boolean.FALSE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

        testHabit.setSchedule(schedule2);

        assertTrue(testHabit.getHabitSchedule()== schedule2); //check if schedule changed
    }

    //Test setAttribute actually sets an attribute
    @Test
    public void testSetAttribute(){
        String name = "name";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        String attribute2 = "Physical";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

        testHabit.setAttribute(attribute2);
        assertTrue(testHabit.getHabitAttribute()== attribute2); //check attribute match
    }

    //Tests addHabitEvent adds a HabitEvent to the Habit
    @Test //temporary
    public void testAddHabitEvent(){
        HabitEvent habitEvent = new HabitEvent();
        String name = "name";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

        testHabit.addHabitEvent(habitEvent);
        assertTrue(testHabit.getHabitEvents().contains(habitEvent));
        //checks if habitEvent was added onto the initially empty ArrayList
    }

    //Tests removeHabitEvent if it successfully removes a habitEvent
    @Test //temporary
    public void testRemoveHabitEvent(){
        HabitEvent habitEvent = new HabitEvent();
        String name = "name";
        String reason = "I wanna be the very best";
        String attribute = "Mental";
        LocalDate startDate = LocalDate.now();
        Boolean[] schedule = {Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,
                Boolean.TRUE,Boolean.TRUE,Boolean.TRUE};
        Habit testHabit = new Habit(name,reason,attribute,startDate,schedule);

        testHabit.addHabitEvent(habitEvent); //assuming addHabitEvent works
        testHabit.removeHabitEvent(habitEvent);

        assertFalse(testHabit.getHabitEvents().contains(habitEvent));
        //checks that habitEvent is missing
    }


}
