package com.example.habitup.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * HabitList represents the current user's list of habits. It can be used to add or delete a
 * habit from the user's habit list. It stores the habits in a linked hash map where the
 * habit's name is the key and the habit object is the value.
 */
public class HabitList {

    private LinkedHashMap<String,Habit> habitMap;

    /**
     * Constructor - will initialize a new empty linked hash map for the user's habits.
     */
    public HabitList() {
        this.habitMap = new LinkedHashMap<>();
    }

    /**
     * Adds a new habit to the habits hash map
     * @param habit the Habit object to add
     */
    public int add(Habit habit) {

        if (this.habitMap.containsKey(habit.getHabitName())) {
            return -1;
        } else {
            this.habitMap.put(habit.getHabitName(), habit);
            return 0;
        }
    }

    /**
     * Removes a habit from the habits hash map
     * @param habitName the name of the habit
     */
    public int delete(String habitName) {

        if (this.habitMap.containsKey(habitName)) {
            this.habitMap.remove(habitName);
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Returns the size of the habit hash map
     * @return the hash map size
     */
    public int size() {
        return this.habitMap.size();
    }

    /**
     * Checks if the habits hash map contains a habit name
     * @param habitName the name of a habit
     * @return True if the hash map contains the habit name
     */
    public boolean containsName(String habitName) {
        return this.habitMap.containsKey(habitName);
    }

    /**
     * Returns an hash map of habits
     * @return the habits hash map
     */
    public ArrayList<Habit> getHabits() {

        return new ArrayList<>(this.habitMap.values());
    }

    /**
     * Returns an hash map of habit name strings
     * @return the hash map of habit names
     */
    public ArrayList<String> getHabitNames() {
        return new ArrayList<>(this.habitMap.keySet());
    }

    /**
     * Returns a specific Habit based on the habit name
     * @param habitName the name of the habit
     * @return the Habit object matching the habit name
     */
    public Habit getHabit(String habitName) {
        return this.habitMap.get(habitName);
    }

    public Habit getHabitByHID(int hid){
        ArrayList<String> habitNames = getHabitNames();
        for (String habitName:habitNames){
            if (getHabit(habitName).getHID() == hid){
                return getHabit(habitName);
            }
        }
        return null;
    }

}
