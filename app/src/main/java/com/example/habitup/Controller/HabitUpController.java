package com.example.habitup.Controller;


import android.util.Log;
import android.widget.Toast;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;

import java.time.LocalDate;
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

        if (!habitAlreadyExists(h)) {
            ElasticSearchController.AddHabitsTask addHabit = new ElasticSearchController.AddHabitsTask();
            addHabit.execute(h);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: a Habit with this name already exists!");
        }
    }

    static public int editHabit(Habit h, boolean nameUnchanged) throws IllegalArgumentException {

        // Name was changed: check to make sure new name is unique
        if (!nameUnchanged) {
            if (habitAlreadyExists(h)) {
                throw new IllegalArgumentException("Error: a Habit with this name already exists!");
            }
        }

        ElasticSearchController.AddHabitsTask addHabit = new ElasticSearchController.AddHabitsTask();
        addHabit.execute(h);
        return 0;
    }

    static public int deleteHabit(Habit h) {
        Log.d("HABIT DELETE:", "Deleting habit " + h.getHabitName());
        ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();
        // Get user habit events
        ElasticSearchController.GetHabitEventsByUidTask getHabitEvents = new ElasticSearchController.GetHabitEventsByUidTask();
        getHabitEvents.execute(HabitUpApplication.getCurrentUIDAsString());
        try {
            events = getHabitEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEvent - Couldn't get HabitEvents");
        }
        ElasticSearchController.DeleteHabitTask delHabit = new ElasticSearchController.DeleteHabitTask();
        for (HabitEvent e : events){
            if (e.getHID() == h.getHID()){
                HabitUpController.deleteHabitEvent(e); // ES delete on associated habit events
            }
        }
        delHabit.execute(Integer.toString(h.getHID()));
        return 0;
    }

    static public int deleteHabitEventsForHabit(Habit h) {
        ElasticSearchController.GetHabitEventsForDelete getDeleteEvents = new ElasticSearchController.GetHabitEventsForDelete();
        getDeleteEvents.execute(String.valueOf(h.getHID()));
        ArrayList<HabitEvent> eventsToDelete = null;

        try {
            eventsToDelete = getDeleteEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtl/deleteHabitEventsForHabit - couldn't get eventsToDelete");
        }

        Log.i("HabitUpDEBUG", "HUCtl/deleteHabitEventsForHabit - found " + String.valueOf(eventsToDelete.size()) + " matches.");

        for (HabitEvent ev : eventsToDelete) {
            ElasticSearchController.DeleteHabitEventTask deleter = new ElasticSearchController.DeleteHabitEventTask();
            deleter.execute(ev.getEID());
        }

        return 0;
    }

    static public int addHabitEvent(HabitEvent event) {
//        Log.d("EVENT:", "Adding HabitEvent to HID #" + String.valueOf(event.getHID()));

        if (!habitEventAlreadyExists(event)) {

            // Add the HabitEvent object to ES
            ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
            addHabitEvent.execute(event);

            // Increment User XP and write back
            UserAccount currentUser = HabitUpApplication.getCurrentUser();

            if (currentUser.getXP() + 1 >= currentUser.getXPtoNext()) {
                currentUser.incrementLevel();
                currentUser.setXPtoNext();
            }

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

        } else {
            throw new IllegalArgumentException("Error: this Habit has already been completed on this date.");
        }
    }

    // Current implementation of AddHabitEventsTask checks to see if event has an EID, and if so,
    // updates the existing one; otherwise creates it and assigns EID.
    static public int editHabitEvent(HabitEvent event) {

        if (!habitEventAlreadyExists(event)) {
            ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
            addHabitEvent.execute(event);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: this Habit has already been completed on this date.");
        }
    }

    static public int deleteHabitEvent(HabitEvent event) {
        Log.d("EVENT DELETE:", "Deleting HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        ElasticSearchController.DeleteHabitEventTask delHabitEvent = new ElasticSearchController.DeleteHabitEventTask();
        delHabitEvent.execute(event.getEID());
        return 0;
    }

    static public boolean habitAlreadyExists(Habit h) {
        ElasticSearchController.GetHabitsByNameForCurrentUserTask checkHabit = new ElasticSearchController.GetHabitsByNameForCurrentUserTask();
        checkHabit.execute(h.getHabitName());
        ArrayList<Habit> matched = null;
        try {
            matched = checkHabit.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtl/addHabit - Execute check failed");
        }

//        for (Habit match : matched) {
//            Log.i("HabitUpDEBUG", "matched habit: " + match.getHabitName());
//        }

        return matched.size() > 0;
    }

    static public boolean habitEventAlreadyExists(HabitEvent event) {
//        ElasticSearchController.GetHabitsEventDuplicates getDupes = new ElasticSearchController.GetHabitsEventDuplicates();
//        getDupes.execute(event);
//        ArrayList<HabitEvent> matches = null;
//        try {
//            matches = getDupes.get();
//        } catch (Exception e) {
//            Log.i("HabitUpDEBUG", "HUCtl/addHabitEvent - exception in getting dupes");
//        }
//
//        Log.i("HabitUpDEBUG", "HUCtl/addHabitEvent - found " + String.valueOf(matches.size()) + " matches.");

        ElasticSearchController.GetHabitEventsByHidTask getEventsForHabit = new ElasticSearchController.GetHabitEventsByHidTask();
        getEventsForHabit.execute(String.valueOf(event.getHID()));
        ArrayList<HabitEvent> matchedEvents = null;
        try {
            matchedEvents = getEventsForHabit.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtl/addHabitEvent - exception in events for same habit");
        }

        boolean alreadyExists = false;
        for (HabitEvent ev : matchedEvents) {
            if (ev.getCompletedate().equals(event.getCompletedate()) && !ev.getEID().equals(event.getEID())) {
                alreadyExists = true;
            }
        }

        return alreadyExists;
    }

    static public boolean habitDoneToday(Habit h) {

//        Log.i("HabitUpDEBUG", "HUCtl/habitDoneToday - in method");

        ElasticSearchController.GetHabitEventsByHidTask getEventsForHabit = new ElasticSearchController.GetHabitEventsByHidTask();
        getEventsForHabit.execute(String.valueOf(h.getHID()));
        ArrayList<HabitEvent> matchedEvents = null;
        try {
            matchedEvents = getEventsForHabit.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtl/addHabitEvent - exception in events for same habit");
        }

        boolean alreadyExists = false;
        for (HabitEvent ev : matchedEvents) {
//            Log.i("HabitUpDEBUG", "HUCtl/habitDoneToday - looking at HabitEvent " + ev.getEID());
            if (ev.getCompletedate().equals(LocalDate.now())) {
                alreadyExists = true;
//                Log.i("HabitUpDEBUG", "HUCtl/habitDoneToday - HabitEvent " + ev.getEID() + " was done today.");
            }
        }

        return alreadyExists;
    }


}
