package com.example.habitup.Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sharidanbarboza on 2017-11-16.
 */

public class HabitEventList {

    private ArrayList<HabitEvent> eventList;

    public HabitEventList() {
        eventList = new ArrayList<>();
    }

    public int size() {
        return this.eventList.size();
    }

    public void add(HabitEvent event) {
        this.eventList.add(event);
    }

    public void delete(HabitEvent event) {
        this.eventList.remove(event);
    }

    public HabitEvent get(int position) {
        return this.eventList.get(position);
    }

    public Boolean contains(HabitEvent event) {
        return this.eventList.contains(event);
    }

    public ArrayList<HabitEvent> getEvents() {
        return this.eventList;
    }

    public ArrayList<HabitEvent> getEventsFromHabit(int hid) {
        ArrayList<HabitEvent> eventsMatched = new ArrayList<>();
        for (HabitEvent event : this.eventList) {
            if (event.getHID() == hid) {
                eventsMatched.add(event);
            }
        }
        return eventsMatched;
    }

    public HabitEvent getRecentEventFromHabit(int hid) {
        ArrayList<HabitEvent> eventsMatched = getEventsFromHabit(hid);
        Collections.sort(eventsMatched);

        if (eventsMatched.size() > 0) {
            return eventsMatched.get(0);
        } else {
            return null;
        }
    }
}
