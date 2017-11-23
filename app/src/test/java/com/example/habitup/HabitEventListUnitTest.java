package com.example.habitup;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventList;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

public class HabitEventListUnitTest {

    @Test
    public void testAddHabitEvent() {
        HabitEventList eventList = new HabitEventList();
        assertTrue(eventList.size() == 0);

        int uid = 1;
        int hid = 1;
        HabitEvent event1 = new HabitEvent(uid, hid);

        eventList.add(event1);
        assertTrue(eventList.contains(event1));
        assertTrue(eventList.size() == 1);
    }

    @Test
    public void testDeleteHabitEvent() {
        HabitEventList eventList = new HabitEventList();
        assertTrue(eventList.size() == 0);

        int uid = 1;
        int hid = 1;
        HabitEvent event1 = new HabitEvent(uid, hid);

        eventList.add(event1);
        assertTrue(eventList.size() == 1);

        eventList.delete(event1);
        assertTrue(!eventList.contains(event1));
        assertTrue(eventList.size() == 0);
    }

    @Test
    public void testGetHabitEvent() {
        HabitEventList eventList = new HabitEventList();
        assertTrue(eventList.size() == 0);

        int uid = 1;
        int hid = 1;
        HabitEvent event1 = new HabitEvent(uid, hid);
        eventList.add(event1);

        HabitEvent event = eventList.get(0);
        assertTrue(event == event1);
    }

    @Test
    public void testGetEventsFromHabit() {
        HabitEventList eventList = new HabitEventList();
        int uid = 1;

        Habit habit1 = new Habit(1);
        Habit habit2 = new Habit(2);

        HabitEvent event1 = new HabitEvent(uid, habit1.getHID());
        HabitEvent event2 = new HabitEvent(uid, habit2.getHID());

        eventList.add(event1);
        eventList.add(event2);

        ArrayList<HabitEvent> events = eventList.getEventsFromHabit(habit1.getHID());
        assertTrue(events.size() == 2);
        assertTrue(events.get(0).getHID() == habit1.getHID());
    }

    @Test
    public void testGetMostRecentEventFromHabit() {
        HabitEventList eventList = new HabitEventList();
        int uid = 1;
        int hid = 1;

        HabitEvent event1 = new HabitEvent(uid, hid);
        HabitEvent event2 = new HabitEvent(uid, hid);

        LocalDate date1 = LocalDate.of(2017,11,10);
        event1.setCompletedate(date1);

        LocalDate date2 = LocalDate.of(2017,11,11);
        event2.setCompletedate(date2);

        eventList.add(event1);
        eventList.add(event2);

        HabitEvent recentEvent = eventList.getRecentEventFromHabit(hid);

        for (int i = 0; i < eventList.size() - 1; i++) {
            HabitEvent otherEvent = eventList.get(i);
            assertTrue(otherEvent.getCompletedate().isBefore(recentEvent.getCompletedate()));
        }
    }

    @Test
    public void testUpdateHabitStrings() {
        HabitEventList eventList = new HabitEventList();
        int uid = 1;

        String habitName1 = "Go to the gym";
        String habitName2 = "Go to class";
        String attribute1 = "Physical";
        String attribute2 = "Discipline";

        Habit habit1 = new Habit(uid);
        habit1.setHabitName(habitName1);
        habit1.setAttribute(attribute1);

        HabitEvent event1 = new HabitEvent(uid,habit1.getHID());
        event1.setHabitStrings(habit1);
        assertTrue(event1.getHabitName().equals(habitName1));
        assertTrue(event1.getHabitAttribute().equals(attribute1));

        eventList.add(event1);

        habit1.setHabitName(habitName2);
        habit1.setAttribute(attribute2);
        eventList.updateEvents(habit1);

        HabitEvent updatedEvent1 = eventList.get(0);
        assertTrue(updatedEvent1.getHabitName().equals(habitName2));
        assertTrue(updatedEvent1.getHabitAttribute().equals(attribute2));

    }
}
