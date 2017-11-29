package com.example.habitup.Controller;


import android.util.Log;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventList;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;

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
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        ArrayList<Habit> habits = new ArrayList<>();
        ArrayList<Habit> allHabits = currentUser.getHabitList().getHabits();

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
            UserAccount currentUser = HabitUpApplication.getCurrentUser();
            currentUser.getHabitList().add(h);

            ElasticSearchController.AddHabitsTask addHabit = new ElasticSearchController.AddHabitsTask();
            addHabit.execute(h);

            updateUser();

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

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        currentUser.getEventList().updateEvents(h);

        ElasticSearchController.AddHabitsTask addHabit = new ElasticSearchController.AddHabitsTask();
        addHabit.execute(h);

        updateUser();
        return 0;
    }

    /**
     * Delete a Habit.
     * @param h Habit to delete
     * @return int successCode (0 for success)
     */
    static public int deleteHabit(Habit h) {

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        currentUser.getHabitList().delete(h.getHabitName());

        ElasticSearchController.DeleteHabitTask delHabit = new ElasticSearchController.DeleteHabitTask();
        delHabit.execute(Integer.toString(h.getHID()));

        updateUser();

        return 0;
    }

    /**
     * Delete all the HabitEvents associated with a Habit that is being deleted
     * @param h Habit being deleted
     * @return int successCode (0 for success)
     */
    static public int deleteHabitEventsForHabit(Habit h) {
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        ArrayList<HabitEvent> eventsToDelete = currentUser.getEventList().getEventsFromHabit(h.getHID());

        for (HabitEvent ev : eventsToDelete) {
            ElasticSearchController.DeleteHabitEventTask deleter = new ElasticSearchController.DeleteHabitEventTask();
            deleter.execute(ev.getEID());
            currentUser.getEventList().delete(ev);
        }

        updateUser();

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
    static public int addHabitEvent(HabitEvent event, Habit habit) throws IllegalArgumentException {

        // Check if HabitEvent is completed before Habit start date
        try {
            if (habitEventBeforeHabitStartDate(event, habit)) {
                throw new IllegalArgumentException("Error: Habit cannot be completed before its start date.");
            }
        } catch (Exception e) {
            // Pass any exception from habitEventBeforeHabitStartDate to caller
            throw new IllegalArgumentException(e.getMessage());
        }

        // Check if HabitEvent for the parent Habit already exists on that day
        if (!habitEventAlreadyExists(event, habit)) {

            // Add the HabitEvent object to ES
            ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
            addHabitEvent.execute(event);
            UserAccount currentUser = HabitUpApplication.getCurrentUser();
            currentUser.getEventList().add(event);

            // Update habit status
            event.setScheduled();
            if (event.getScheduled()) {
                habit.incrementHabitsDone();
            } else {
                habit.incrementHabitsDoneExtra();
            }

            if (currentUser.getXP() + 1 >= currentUser.getXPtoNext()) {
                currentUser.incrementLevel();
                currentUser.setXPtoNext();
            }

            currentUser.increaseXP(HabitUpApplication.XP_PER_HABITEVENT);

            // Setup for attribute increment: need the Habit's Attribute type
            String attrName = habit.getHabitAttribute();

            // Increment User Attribute
            HabitUpApplication.updateCurrentAttrs();
            HabitUpApplication.getCurrentAttrs().increaseValueBy(attrName, HabitUpApplication.ATTR_INCREMENT_PER_HABITEVENT);

            ElasticSearchController.AddAttrsTask writeAttrs = new ElasticSearchController.AddAttrsTask();
            writeAttrs.execute(HabitUpApplication.getCurrentAttrs());

            updateUser();

            return 0;

        } else {
            throw new IllegalArgumentException("Error: this Habit has already been completed on this date.");
        }
    }

    static public boolean levelUp() {
        // Increment User XP and write back
        boolean levelledUp = false;
        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        if (currentUser.getXP() + 1 >= currentUser.getXPtoNext()) {
            currentUser.incrementLevel();
            currentUser.setXPtoNext();
            levelledUp = true;
        }

        updateUser();

        return levelledUp;
    }

    /**
     * Edit an existing HabitEvent - in case the date was edited, checks to make sure no HabitEvent
     * belonging to the same Habit exists for the set date, unless it is the same HabitEvent that is
     * being updated here.
     * @param event HabitEvent
     * @return int successCode (0 for success)
     * @throws IllegalArgumentException if a different HabitEvent exists for the same Habit on that date
     */
    static public int editHabitEvent(HabitEvent event, Habit habit) throws IllegalArgumentException {

        // Check if HabitEvent is completed before Habit start date
        try {
            if (habitEventBeforeHabitStartDate(event, habit)) {
                throw new IllegalArgumentException("Error: Habit cannot be completed before its start date.");
            }
        } catch (Exception e) {
            // Pass any exception from habitEventBeforeHabitStartDate to caller
            throw new IllegalArgumentException(e.getMessage());
        }

        // Check if HabitEvent for the parent Habit already exists on that day
        if (!habitEventAlreadyExists(event, habit)) {
            ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
            addHabitEvent.execute(event);
            event.setHabitStrings(habit);
            updateUser();

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
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        currentUser.getEventList().delete(event);

//        Log.d("EVENT DELETE:", "Deleting HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        ElasticSearchController.DeleteHabitEventTask delHabitEvent = new ElasticSearchController.DeleteHabitEventTask();
        delHabitEvent.execute(event.getEID());

        updateUser();

        return 0;
    }

    /**
     * Utility method to see if a Habit with the same name already exists for the current user.
     * @param h Habit to check
     * @return True if a Habit with that name already exists
     */
    static public boolean habitAlreadyExists(Habit h) {
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        return currentUser.getHabitList().containsName(h.getHabitName());
    }

    /**
     * Utility method to see if a different HabitEvent already exists for the same Habit on the
     * same date.
     * @param event HabitEvent to check
     * @return True if a different HabitEvent already exists on that date
     */
    static public boolean habitEventAlreadyExists(HabitEvent event, Habit habit) {
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        ArrayList<HabitEvent> matchedEvents = currentUser.getEventList().getEventsFromHabit(habit.getHID());

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
    static private boolean habitEventBeforeHabitStartDate(HabitEvent event, Habit habit) throws RuntimeException {
        return habit.getStartDate().isAfter(event.getCompletedate());
    }

    /**
     * Utility method to check whether a Habit has a HabitEvent for the current day.
     * @param h Habit to check
     * @return True if the Habit has a HabitEvent already for the current day
     */
    static public boolean habitDoneToday(Habit h) {

//        Log.i("HabitUpDEBUG", "HUCtl/habitDoneToday - in method");

        HabitEventList eventList = HabitUpApplication.getCurrentUser().getEventList();
        HabitEvent recentEvent = eventList.getRecentEventFromHabit(h.getHID());

        if (recentEvent != null) {
            return recentEvent.getCompletedate().equals(LocalDate.now());
        } else {
            return false;
        }
    }

    /**
     * Updates the current user's model in ElasticSearch
     */
    static public void updateUser() {
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        ElasticSearchController.AddUsersTask updateUser = new ElasticSearchController.AddUsersTask();
        updateUser.execute(currentUser);
    }

}

