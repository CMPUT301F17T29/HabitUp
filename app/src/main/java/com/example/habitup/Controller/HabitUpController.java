package com.example.habitup.Controller;


import android.util.Log;
import android.widget.Toast;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;

import java.util.ArrayList;

public class HabitUpController {



    public HabitUpController() {

    }

    static public ArrayList<Habit> getTodaysHabits() {
        ArrayList<Habit> habits = new ArrayList<>();
        ArrayList<Habit> allHabits;
        ElasticSearchController.GetUserHabitsTask getUserHabits = new ElasticSearchController.GetUserHabitsTask();
        getUserHabits.execute(String.valueOf(HabitUpApplication.getCurrentUID()));

        try {
            allHabits = getUserHabits.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCont - couldn't get Habits");
            return null;
        }

        for (Habit habit : allHabits) {
            Log.i("HabitUpDEBUG", "getTodaysHabits: " + habit.getHabitName());
            if (habit.isTodayHabit()) { habits.add(habit); }
        }

        return habits;
    }

    static public int addHabit(Habit h) {
        ElasticSearchController.AddHabitsTask addHabit = new ElasticSearchController.AddHabitsTask();
        addHabit.execute(h);
        return 0;
    }

    static public int editHabut(Habit h) {
        Log.d("HABIT EDIT:", "Editing habit " + h.getHabitName());
        return 0;
    }

    static public int deleteHabit(Habit h) {
        Log.d("HABIT DELETE:", "Deleting habit " + h.getHabitName());
        return 0;
    }

    static public int addHabitEvent(HabitEvent event) {
        Log.d("EVENT:", "Adding HabitEvent to HID #" + String.valueOf(event.getHID()));
        ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
        addHabitEvent.execute(event);
        return 0;
    }

    static public int editHabitEvent(HabitEvent event) {
        Log.d("EVENT EDIT:", "Editing HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        return 0;
    }

    static public int deleteHabitEvent(HabitEvent event) {
        Log.d("EVENT DELETE:", "Deleting HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        return 0;
    }


}
