package com.example.habitup.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * HabitEventList represents the current user's list of habit events. It can be used to add or
 * delete an event from the user's event list. It can also be used to get events corresponding
 * to a specific habit and getting the most recent event based on the habit type.
 *
 * @author barboza
 */
public class HabitEventList {

    private ArrayList<HabitEvent> eventList;

    /**
     * Constructor - will initialize a new empty array list for habit events
     */
    public HabitEventList() {
        eventList = new ArrayList<>();
    }

    /**
     * Returns the size of the events array list
     * @return the events size
     */
    public int size() {
        return this.eventList.size();
    }

    /**
     * Adds a new habit events to the events array list
     * @param event a HabitEvent object to add
     */
    public void add(HabitEvent event) {

        for(int i=0; i<this.eventList.size(); i++){
            HabitEvent listEvent = eventList.get(i);
            if(event.getCompletedate().isAfter((listEvent.getCompletedate()))){
                //found the insert point
                this.eventList.add(i,event);
                Log.i("HabitEventListDebug", "adding habit event at index: "+i);
                return;
            }

        }
        Log.i("HabitEventListDebug", "Length of eventList after adding is: "+eventList.size());

        this.eventList.add(event);
    }

    /**
     * Removes a habit event from the events array list
     * @param event a HabitEvent object to delete
     */
    public void delete(HabitEvent event) {
        this.eventList.remove(event);
    }

    /**
     * Gets a specific habit event at a position in the array list
     * @param position the index in the events array list
     * @return a HabitEvent object
     */
    public HabitEvent get(int position) {
        return this.eventList.get(position);
    }

    /**
     * Checks if a habit event exists in the current events array list
     * @param event a HabitEvent object
     * @return true if the habit events exists in the events array list
     */
    public Boolean contains(HabitEvent event) {
        return this.eventList.contains(event);
    }

    /**
     * Returns an array list of habit events
     * @return the events array list
     */
    public ArrayList<HabitEvent> getEvents() {
        return this.eventList;
    }

    /**
     * Get an array list of habit events all belonging to the same habit
     * @param hid the Habit id
     * @return an array list of habit events
     */
    public ArrayList<HabitEvent> getEventsFromHabit(int hid) {
        ArrayList<HabitEvent> eventsMatched = new ArrayList<>();
        for (HabitEvent event : this.eventList) {
            if (event.getHID() == hid) {
                eventsMatched.add(event);
            }
        }
        return eventsMatched;
    }

    /**
     * Returns the most recent habit events from a specific habit
     * @param hid the Habit id
     * @return the HabitEvent object most recently completed based on the habit type
     */
    public HabitEvent getRecentEventFromHabit(int hid) {
        ArrayList<HabitEvent> eventsMatched = getEventsFromHabit(hid);
        Collections.sort(eventsMatched);

        if (eventsMatched.size() > 0) {
            return eventsMatched.get(0);
        } else {
            return null;
        }
    }

    /**
     * Updates the names and attributes of the events that belong to a habit
     * @param habit the Habit that was updated
     */
    public void updateEvents(Habit habit) {
        ArrayList<HabitEvent> eventsMatched = getEventsFromHabit(habit.getHID());
        for (HabitEvent event : eventsMatched) {
            event.setHabitStrings(habit);
        }
    }

    public void sort(){
        for (HabitEvent event : eventList){}
    }
}
