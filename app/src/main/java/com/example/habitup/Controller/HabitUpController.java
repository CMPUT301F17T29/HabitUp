package com.example.habitup.Controller;


import android.util.Log;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * HabitUpController is used for core functionality relating to Habits and HabitEvents.  It is the
 * primary controller linking the Views and Models, and is also often the layer between the app and
 * ElasticSearch, through invocations to ElasticSearchController, for common and transactional
 * operations.
 *
 * @author @gojeffcho
 *
 * Javadoc last updated 2017-11-13 by @gojeffcho.
 */

public class HabitUpController {

    /**
     * Get all Habits that are scheduled to be done on the current day for the logged-in user.
     * Used for MainActivity Habit list.
     * @return ArrayList<Habit> of today's Habits
     */
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

    /**
     * Add a new Habit and send it to the DB.
     * @param h Habit to be added
     * @return int successCode (0 for success)
     * @throws IllegalArgumentException if Habit name is not unique
     */
    static public int addHabit(Habit h) throws IllegalArgumentException {

        if (!habitAlreadyExists(h)) {
            ElasticSearchController.AddHabitsTask addHabit = new ElasticSearchController.AddHabitsTask();
            addHabit.execute(h);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: a Habit with this name already exists!");
        }
    }

    /**
     * Edit an existing Habit.  If name was not changed, update existing Habit.  If name was
     * changed, check to ensure the new name is unique, then update the Habit accordingly.
     * @param h Habit after edits
     * @param nameUnchanged True if name did not update - important for uniqueness check
     * @return int successCode (0 for success)
     * @throws IllegalArgumentException if name was changed but not unique
     */
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

    /**
     * Delete a Habit.
     * @param h Habit to delete
     * @return int successCode (0 for success)
     */
    static public int deleteHabit(Habit h) {
        ElasticSearchController.DeleteHabitTask delHabit = new ElasticSearchController.DeleteHabitTask();
        delHabit.execute(Integer.toString(h.getHID()));
        return 0;
    }

    /**
     * Delete all the HabitEvents associated with a Habit that is being deleted
     * @param h Habit being deleted
     * @return int successCode (0 for success)
     */
    static public int deleteHabitEventsForHabit(Habit h) {
        ElasticSearchController.GetHabitEventsForDelete getDeleteEvents = new ElasticSearchController.GetHabitEventsForDelete();
        getDeleteEvents.execute(String.valueOf(h.getHID()));
        ArrayList<HabitEvent> eventsToDelete = null;

        try {
            eventsToDelete = getDeleteEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtl/deleteHabitEventsForHabit - couldn't get eventsToDelete");
        }

//        Log.i("HabitUpDEBUG", "HUCtl/deleteHabitEventsForHabit - found " + String.valueOf(eventsToDelete.size()) + " matches.");

        for (HabitEvent ev : eventsToDelete) {
            ElasticSearchController.DeleteHabitEventTask deleter = new ElasticSearchController.DeleteHabitEventTask();
            deleter.execute(ev.getEID());
        }

        return 0;
    }

    /**
     * Add a new HabitEvent, as long as no HabitEvent from that Habit already exists for the day it
     * is being added for.  XP and associated Attribute are incremented if the HabitEvent is valid,
     * and if user's next XP target is met, their level is incremented and their target XP is also
     * increased.
     *
     * @param event HabitEvent to add
     * @return int successCode (0 for success)
     * @throws IllegalArgumentException if HabitEvent already exists for Habit on that day
     */
    static public int addHabitEvent(HabitEvent event) throws IllegalArgumentException {
//        Log.d("EVENT:", "Adding HabitEvent to HID #" + String.valueOf(event.getHID()));

        // Check if HabitEvent is completed before Habit start date
        try {
            if (habitEventBeforeHabitStartDate(event)) {
                throw new IllegalArgumentException("Error: Habit cannot be completed before its start date.");
            }
        } catch (Exception e) {
            // Pass any exception from habitEventBeforeHabitStartDate to caller
            throw new IllegalArgumentException(e.getMessage());
        }

        // Check if HabitEvent for the parent Habit already exists on that day
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

    /**
     * Edit an existing HabitEvent - in case the date was edited, checks to make sure no HabitEvent
     * belonging to the same Habit exists for the set date, unless it is the same HabitEvent that is
     * being updated here.
     * @param event HabitEvent
     * @return int successCode (0 for success)
     * @throws IllegalArgumentException if a different HabitEvent exists for the same Habit on that date
     */
    static public int editHabitEvent(HabitEvent event) throws IllegalArgumentException {

        // Check if HabitEvent is completed before Habit start date
        try {
            if (habitEventBeforeHabitStartDate(event)) {
                throw new IllegalArgumentException("Error: Habit cannot be completed before its start date.");
            }
        } catch (Exception e) {
            // Pass any exception from habitEventBeforeHabitStartDate to caller
            throw new IllegalArgumentException(e.getMessage());
        }

        // Check if HabitEvent for the parent Habit already exists on that day
        if (!habitEventAlreadyExists(event)) {
            ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
            addHabitEvent.execute(event);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: this Habit has already been completed on this date.");
        }
    }

    /**
     * Delete a HabitEvent.
     * @param event HabitEvent to delete
     * @return successCode (0 for success)
     */
    static public int deleteHabitEvent(HabitEvent event) {
//        Log.d("EVENT DELETE:", "Deleting HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        ElasticSearchController.DeleteHabitEventTask delHabitEvent = new ElasticSearchController.DeleteHabitEventTask();
        delHabitEvent.execute(event.getEID());
        return 0;
    }

    /**
     * Utility method to see if a Habit with the same name already exists for the current user.
     * @param h Habit to check
     * @return True if a Habit with that name already exists
     */
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

    /**
     * Utility method to see if a different HabitEvent already exists for the same Habit on the
     * same date.
     * @param event HabitEvent to check
     * @return True if a different HabitEvent already exists on that date
     */
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

    /**
     * Utility method to check whether a HabitEvent's completedate is before a Habit's startDate
     * @param event HabitEvent to check
     * @return True if HabitEvent happens before the Habit's startDate
     * @throws RuntimeException if parentHabit could not be obtained
     */
    static private boolean habitEventBeforeHabitStartDate(HabitEvent event) throws RuntimeException {
        ElasticSearchController.GetHabitsTask getHabit = new ElasticSearchController.GetHabitsTask();
        getHabit.execute(String.valueOf(event.getHID()));
        Habit parentHabit = null;
        try {
            parentHabit = getHabit.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "HUCtl/habitEventBeforeHabitStartDate - exception getting parentHabit");
        }

        if (parentHabit == null) {
            throw new RuntimeException("Couldn't get parent Habit for this HabitEvent.");
        }

        return parentHabit.getStartDate().isAfter(event.getCompletedate());
    }

    /**
     * Utility method to check whether a Habit has a HabitEvent for the current day.
     * @param h Habit to check
     * @return True if the Habit has a HabitEvent already for the current day
     */
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
                break;
            }
        }
        return alreadyExists;
    }


}
