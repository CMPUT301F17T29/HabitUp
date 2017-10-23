package com.example.habitup.Model;

import java.util.ArrayList;

/**
 * Created by gojeffcho on 2017-10-20.
 *
 * HabitEventList: container for HabitEvents, used for things like HabitHistory
 * Current implementation uses ArrayList
 */

public class HabitEventList {

    // Members
    private ArrayList<HabitEvent> habitList;

    /**
     * Constructor: create new ArrayList<> (current implementation)
     */
    public HabitEventList() {
        habitList = new ArrayList<>();
    }

    /**
     * Get the number of HabitEvents in the HabitEventList
     * @return Int
     */
    public int size() {
        return habitList.size();
    }

    /**
     * Add a HabitEvent to the list
     * @param eventToAdd
     */
    public void add(HabitEvent eventToAdd) {
        habitList.add(eventToAdd);
    }

    /**
     * Check whether HabitEventList contains a certain HabitEvent
     * @param event
     * @return True if contained; otherwise False
     */
    public Boolean contains(HabitEvent event) {
        return habitList.contains(event);
    }

    /**
     * Remove a HabitEvent from the HabitEventList if it exists
     * @param event
     */
    public void remove(HabitEvent event) {
        habitList.remove(event);
    }

    /**
     * Return an ArrayList of HabitEvents represented by the HabitEventList
     * @return
     */
    public ArrayList<HabitEvent> getHabitEvents() {
        ArrayList<HabitEvent> returnList = new ArrayList<>(habitList.size());
        for (HabitEvent e : habitList) {
            returnList.add(new HabitEvent(e));
        }
        return returnList;
    }


}
