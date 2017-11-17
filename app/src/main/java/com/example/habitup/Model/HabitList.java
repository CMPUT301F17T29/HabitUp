package com.example.habitup.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by sharidanbarboza on 2017-11-16.
 */

public class HabitList {

    private LinkedHashMap<String,Habit> habitMap;

    public HabitList() {
        this.habitMap = new LinkedHashMap<>();
    }

    public void add(Habit habit) {
        this.habitMap.put(habit.getHabitName(), habit);
    }

    public void delete(Habit habit) {
        this.habitMap.remove(habit);
    }

    public boolean containsName(String habitName) {
        return this.habitMap.containsKey(habitName);
    }

    public boolean containsHabit(Habit habit) {
        return this.habitMap.containsValue(habit);
    }

    public ArrayList<Habit> getHabits() {
        return new ArrayList<>(this.habitMap.values());
    }

    public ArrayList<String> getHabitNames() {
        return new ArrayList<>(this.habitMap.keySet());
    }

    public Habit getHabit(String habitName) {
        return this.habitMap.get(habitName);
    }
}
