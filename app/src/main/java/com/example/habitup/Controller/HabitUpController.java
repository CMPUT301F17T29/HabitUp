package com.example.habitup.Controller;


import android.util.Log;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;

public class HabitUpController {



    public HabitUpController() {

    }

    static public int addHabit(Habit h) {
        HabitUpApplication.currentUser.getHabits().add(h);
        Log.d("DEBUG", "Adding habit " + h.getHabitName());
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
