package com.example.habitup.Model;

import java.util.ArrayList;

/**
 * Created by gojeffcho on 2017-10-23.
 */

public class HabitList {

    private ArrayList<Habit> habits;

    /**
     * Constructor
     */
    public HabitList() {
        habits = new ArrayList<>();
    }

    /**
     * Add a Habit to the HabitList if it is not already there
     * Return True if unique and added; return False if not
     * @param h Habit
     * @return Boolean
     */
    public Boolean add(Habit h) {
        // Test for Habit not already in list
        // Throw exception if it is
        // Add habit to list
        return true;
    }

    /**
     * Delete a habit from the list if it exists and returns True, otherwise returns False
     * @param h
     * @return Boolean
     */
    public Boolean remove(Habit h) {
        // Check if habit is in list
        // If not, throw exception
        // If yes, delete it
        return true;
    }

    /**
     * Checks to see if habit is in HabitList
     * @param h
     * @return Boolean
     */
    public Boolean contains(Habit h) {
        return habits.contains(h);
    }

    /**
     * Gets the HabitList
     * @return ArrayList<Habit>
     */
    public ArrayList<Habit> getHabits() {
        return this.habits;
    }

    /**
     * Gets the number of elements in the HabitList
     * @return int
     */
    public int size() {
        return this.habits.size();
    }

}
