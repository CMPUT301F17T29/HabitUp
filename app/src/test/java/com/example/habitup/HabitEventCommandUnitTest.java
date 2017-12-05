package com.example.habitup;

import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventCommand;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Ty on 2017-11-12.
 */

public class HabitEventCommandUnitTest {

    //Test setType()
    @Test
    public void testSetType() {
        HabitEvent oldEvent = new HabitEvent(99,99);
        HabitEventCommand cmd = new HabitEventCommand("add", oldEvent, "habit");
        assertTrue(cmd.getType().equals("add"));
        cmd.setType("delete");
        assertTrue(cmd.getType().equals("delete"));
    }

    //Test setHabitName()
    @Test
    public void testSetHabitName() {
        HabitEvent oldEvent = new HabitEvent(99,99);
        HabitEventCommand cmd = new HabitEventCommand("add", oldEvent, "habit");
        assertTrue(cmd.getHabitName().equals("habit"));
        cmd.setHabitName("newHabit");
        assertTrue(cmd.getHabitName().equals("newHabit"));
    }

    //Test Habit
    @Test
    public void testSetEvent() {
        HabitEvent newEvent = new HabitEvent(100,100);
        HabitEvent oldEvent = new HabitEvent(99,99);
        HabitEventCommand cmd = new HabitEventCommand("add", oldEvent, "habit");
        assertTrue(cmd.getEvent().getUID() == 99);
        cmd.setEvent(newEvent);
        assertTrue(cmd.getEvent().getUID() == 100);
    }

    ;
}
