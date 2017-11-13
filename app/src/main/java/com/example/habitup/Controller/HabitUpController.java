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
//            Log.i("HabitUpDEBUG", "getTodaysHabits: " + habit.getHabitName());
            if (habit.isTodayHabit()) { habits.add(habit); }
        }

        return habits;
    }

    static public int addHabit(Habit h) throws IllegalArgumentException {

        ElasticSearchController.GetHabitsByNameForCurrentUserTask checkHabit = new ElasticSearchController.GetHabitsByNameForCurrentUserTask();
        checkHabit.execute(h.getHabitName());
        ArrayList<Habit> matched = null;
        try {
            matched = checkHabit.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtl/addHabit - Execute check failed");
        }

        for (Habit match : matched) {
            Log.i("HabitUpDEBUG", "matched habit: " + match.getHabitName());
        }

        if (matched.size() == 0) {
            ElasticSearchController.AddHabitsTask addHabit = new ElasticSearchController.AddHabitsTask();
            addHabit.execute(h);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: a Habit with this name already exists!");
        }
    }

    static public int deleteHabit(Habit h) {
        Log.d("HABIT DELETE:", "Deleting habit " + h.getHabitName());
        ElasticSearchController.DeleteHabitTask delHabit = new ElasticSearchController.DeleteHabitTask();
        delHabit.execute(Integer.toString(h.getHID()));
        return 0;
    }

    static public int addHabitEvent(HabitEvent event) {
//        Log.d("EVENT:", "Adding HabitEvent to HID #" + String.valueOf(event.getHID()));

        // Add the HabitEvent object to ES
        ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
        addHabitEvent.execute(event);

        // Increment User XP and write back
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        currentUser.increaseXP(HabitUpApplication.XP_PER_HABITEVENT);
        ElasticSearchController.AddUsersTask updateUser = new ElasticSearchController.AddUsersTask();
        updateUser.execute(currentUser);

        // Setup for attribute increment: need the Habit's Attribute type
        ElasticSearchController.GetHabitsTask getHabit = new ElasticSearchController.GetHabitsTask();
        getHabit.execute(String.valueOf(event.getHID()));
        String attrName = "";
        try {
            attrName = getHabit.get().get(0).getHabitAttribute();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtrl / Couldn't get Attribute name for Habit");
        }

        // Increment User Attribute
        if (attrName != "") {
            HabitUpApplication.updateCurrentAttrs();
            HabitUpApplication.getCurrentAttrs().increaseValueBy(attrName, HabitUpApplication.ATTR_INCREMENT_PER_HABITEVENT);
        }

        // Write back User Attribute
        ElasticSearchController.AddAttrsTask writeAttrs = new ElasticSearchController.AddAttrsTask();
        writeAttrs.execute(HabitUpApplication.getCurrentAttrs());

        return 0;
    }

    // Current implementation of AddHabitEventsTask checks to see if event has an EID, and if so,
    // updates the existing one; otherwise creates it and assigns EID.
    static public int editHabitEvent(HabitEvent event) {
        Log.d("EVENT EDIT:", "Editing HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
        addHabitEvent.execute(event);
        return 0;
    }

    static public int deleteHabitEvent(HabitEvent event) {
        Log.d("EVENT DELETE:", "Deleting HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        ElasticSearchController.DeleteHabitEventTask delHabitEvent = new ElasticSearchController.DeleteHabitEventTask();
        delHabitEvent.execute(event.getEID());
        return 0;
    }


}
