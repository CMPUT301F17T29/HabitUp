package com.example.habitup.Model;

import java.util.ArrayList;

/**
 * Created by gojeffcho on 2017-10-23.
 * Test
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
     * @return -1 if already in list, 0 if successful
     */
    public int add(Habit h) {
        // Test for Habit not already in list
        if (habits.contains(h)) {
            return -1;
        } else {
            habits.add(h);
            return 0;
        }
    }

    /**
     * Delete a habit from the list if it exists and returns True, otherwise returns False
     * @param h Habit
     * @return 0 if successful, -1 if not in list
     */
    public int remove(Habit h) {
        // Check if habit is in list
        if (habits.contains(h)) {
            habits.remove(h);
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Checks to see if habit is in HabitList
     * @param h Habit
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
     * Gets the Habit object being requested by name
     * @param habitName String
     * @return null if not found, otherwise Habit
     */
    public Habit getHabit(String habitName) {
        for (Habit habit : habits) {
            if (habit.getHabitName() == habitName) {
                return habit;
            }
        }
        return null;
    }

    /**
     * Gets the number of elements in the HabitList
     * @return int
     */
    public int size() {
        return this.habits.size();
    }

}
