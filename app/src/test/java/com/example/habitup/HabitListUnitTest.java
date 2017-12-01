package com.example.habitup;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitList;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

public class HabitListUnitTest {

    @Test
    public void testAddHabit() {
        HabitList habitList = new HabitList();
        assertTrue(habitList.size() == 0);

        String habitName = "Go to gym";
        Habit habit1 = new Habit(1);
        habit1.setHabitName(habitName);
        habitList.add(habit1);

        assertTrue(habitList.size() == 1);
        assertTrue(habitList.containsName(habitName));
    }

    @Test
    public void testDeleteHabit() {
        HabitList habitList = new HabitList();
        assertTrue(habitList.size() == 0);

        String habitName = "Go to gym";
        Habit habit1 = new Habit(1);
        habit1.setHabitName(habitName);
        habitList.add(habit1);

        assertTrue(habitList.size() == 1);
        assertTrue(habitList.containsName(habitName));

        habitList.delete(habitName);
        assertTrue(habitList.size() == 0);
        assertTrue(!habitList.containsName(habitName));
    }

    @Test
    public void testGetHabitNames() {
        HabitList habitList = new HabitList();

        String[] habitNames = {"Go to gym", "Study for class", "Clean room"};

        for (int i = 0; i < habitNames.length; i++) {
            Habit habit = new Habit(i + 1);
            habit.setHabitName(habitNames[i]);
            habitList.add(habit);
        }

        ArrayList<String> returnedNames = habitList.getHabitNames();
        for (String name : returnedNames) {
            assertTrue(habitList.containsName(name));
        }
    }

    @Test
    public void testGetHabit() {
        HabitList habitList = new HabitList();

        String habitName = "Go to gym";
        Habit habit1 = new Habit(1);
        habit1.setHabitName(habitName);
        habitList.add(habit1);

        Habit returnedHabit = habitList.getHabit(habitName);
        assertTrue(returnedHabit.getHID() == habit1.getHID());
    }
}
