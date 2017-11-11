package com.example.habitup.Model;

import java.util.PriorityQueue;

/**
 * Created by gojeffcho on 2017-10-20.
 *
 * HabitEventList: container for HabitEvents, used for things like HabitHistory
 * Current implementation uses PriorityQueue sorted on Date
 */
public class HabitEventList {

    // Members
//    private ArrayList<HabitEvent> habitList;
    private int uid;
    private int hid;
    private PriorityQueue<HabitEvent> habitList;

    /**
     * Constructor: create new ArrayList<> (current implementation)
     */
    public HabitEventList(int uid, int hid) {
        this.uid = uid;
        this.hid = hid;
        habitList = new PriorityQueue<>();
    }

    public int getUID() { return this.uid; }

    public int getHID() { return this.hid; }

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
     * Return an PriorityQueue of HabitEvents represented by the HabitEventList
     * @return
     */
    public PriorityQueue<HabitEvent> getHabitEvents() {
        PriorityQueue<HabitEvent> returnList = new PriorityQueue<>(habitList.size());
        for (HabitEvent e : habitList) {
            returnList.add(new HabitEvent(e));
        }
        return returnList;
    }
}
