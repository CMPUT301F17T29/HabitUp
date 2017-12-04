package com.example.habitup.Controller;


import android.content.Context;
import android.util.Log;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventCommand;
import com.example.habitup.Model.HabitEventList;
import com.example.habitup.Model.UserAccount;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

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
     * Add a habit Event to a Users local list and if we are connected to the internet Send to ES
     * @param event event to be added
     * @param habit the habit of the event
     * @param ctx state of the app, fo connectivity check
     * @return 0 for success
     */
    static public int addHabitEvent(HabitEvent event, Habit habit, Context ctx){
        addHabitEventLocal(event, habit);
        executeCommands(ctx);
        return 0;
    }

    /**
     * Add a habit event to the local list if it passes the error checking
     * @param event event to be added
     * @param habit habit of the event
     * @return 0 for success
     * @throws IllegalArgumentException if the event was completed before the habit start date or
     * if there is already a habit event with that date and type
     */
    static public int addHabitEventLocal(HabitEvent event, Habit habit) throws IllegalArgumentException{

        try {
            if (habitEventBeforeHabitStartDate(event, habit)) {
                throw new IllegalArgumentException("Error: Habit cannot be completed before its start date.");
            }
        } catch (Exception e) {
            // Pass any exception from habitEventBeforeHabitStartDate to caller
            throw new IllegalArgumentException(e.getMessage());
        }

        if(!habitEventAlreadyExists(event, habit)){

            UserAccount currentUser = HabitUpApplication.getCurrentUser();
            currentUser.getEventList().add(event);

            HabitEventCommand cmd = new HabitEventCommand("add", event, habit.getHabitName());
            currentUser.addCommand(cmd);
        }else {
            throw new IllegalArgumentException("Error: this Habit has already been completed on this date.");
        }

        return 0;
    }

    static public int addHabitEventOnline(HabitEvent event, Habit habit, Context ctx) throws IllegalArgumentException {

        if (HabitUpApplication.isOnline(ctx)) {
            UserAccount currentUser = HabitUpApplication.getCurrentUser();

            //event has not been uploaded to es
            ElasticSearchController.AddHabitEventsTask addHabitEvent = new ElasticSearchController.AddHabitEventsTask();
            addHabitEvent.execute(event);

            if (currentUser.getXP() >= currentUser.getXPtoNext()) {
                currentUser.incrementLevel();
                currentUser.setXPtoNext();
            }

            currentUser.increaseXP(HabitUpApplication.XP_PER_HABITEVENT);
            ElasticSearchController.AddUsersTask updateUser = new ElasticSearchController.AddUsersTask();
            updateUser.execute(currentUser);

            // Setup for attribute increment: need the Habit's Attribute type
            String attrName = habit.getHabitAttribute();

            // Increment User Attribute

            HabitUpApplication.updateCurrentAttrs();
            HabitUpApplication.getCurrentAttrs().increaseValueBy(attrName, HabitUpApplication.ATTR_INCREMENT_PER_HABITEVENT);

            ElasticSearchController.AddAttrsTask writeAttrs = new ElasticSearchController.AddAttrsTask();
            writeAttrs.execute(HabitUpApplication.getCurrentAttrs());
            return 0;
        }
        return -1;
    }

    static public int editHabitEvent(LocalDate beforeDate, HabitEvent event, HabitEvent oldHabit, Habit habit, Context ctx) throws IllegalArgumentException{
        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        if(editCompleteDateCheck(beforeDate, event, habit)) {
            deleteHabitEvent(event, habit.getHabitName(), ctx);
            addHabitEvent(event, habit, ctx);

        }else throw new IllegalArgumentException("Error: You cannot edit this habit to that day");


        return 0;
    }

    static public boolean levelUp(Context ctx) {
        // Increment User XP and write back
        boolean levelledUp = false;
        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        if (currentUser.getXP() >= currentUser.getXPtoNext()) {
            currentUser.incrementLevel();
            currentUser.setXPtoNext();
            levelledUp = true;
        }
        if(HabitUpApplication.isOnline(ctx)) {
            updateUser();
        }

        return levelledUp;
    }

    static public int deleteHabitEventLocal(HabitEvent event, String habitName) {

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        currentUser.getEventList().delete(event);

        HabitEventCommand cmd = new HabitEventCommand("delete", event, habitName);
        currentUser.addCommand(cmd);

        return 0;

    }

    static public int deleteHabitEventOnline(HabitEvent event) {

        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        Log.d("EVENT DELETE:", "Deleting HabitEvent belonging to HID #" + String.valueOf(event.getHID()));
        ElasticSearchController.DeleteHabitEventTask delHabitEvent = new ElasticSearchController.DeleteHabitEventTask();
        delHabitEvent.execute(event.getEID());

        ElasticSearchController.AddUsersTask updateUser = new ElasticSearchController.AddUsersTask();
        updateUser.execute(currentUser);

        return 0;

    }

    static public int deleteHabitEvent(HabitEvent event, String habitName, Context ctx) {
        deleteHabitEventLocal(event, habitName);
        executeCommands(ctx);


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
            if ((ev.getCompletedate().equals(event.getCompletedate())) && (ev.getHID()==event.getHID())) {
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

    static public boolean editCompleteDateCheck(LocalDate before, HabitEvent event, Habit habit){
        boolean date = false;
        if (before.equals(event.getCompletedate())){
            date = true;
        }else if(!habitEventAlreadyExists(event, habit)){
            date = true;
        }
        return date;
    }

    /**
     * Utility method to check whether a Habit has a HabitEvent for the current day.
     * @param h Habit to check
     * @return True if the Habit has a HabitEvent already for the current day
     */
    static public boolean habitDoneToday(Habit h) {

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
        HabitUpApplication.updateUser(currentUser);
    }

    /**
     *Updates the local user model and ES based on a queue stored in local data
     */
    static public int executeOldCommands(LinkedList<HabitEventCommand> oldQueue,Context ctx){

        Log.i("DebugOldCommand", "in method");

        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        HabitEventCommand hec = oldQueue.poll();
        while (hec!=null) {

            Habit habit = currentUser.getHabitList().getHabit(hec.getHabitName());

            if (hec.getType().equals("add")) {
                addHabitEvent(hec.getEvent(), habit, ctx);
            }
            else if (hec.getType().equals("delete")) {
                Log.i("DebugOldCommand", "in delete");
                deleteHabitEvent(hec.getEvent(), habit.getHabitName(), ctx);
            }

            hec = oldQueue.poll();
        }
        executeCommands(ctx);
        return 0;
    }

    /**
     *Updates ES based on a queue of events to add or edit when connected to the internet.
     */
    static public int executeCommands(Context ctx){

        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        LinkedList<HabitEventCommand> cQueue = currentUser.getCommandQueue();
        if(cQueue.size()>0){
            if(HabitUpApplication.isOnline(ctx)){
                HabitEventCommand hec = cQueue.poll();
                while (hec!=null) {
                    if (hec.getType().equals("add")) {
                        HabitEvent habitEvent = hec.getEvent();
                        Habit habit = currentUser.getHabitList().getHabit(habitEvent.getHabitName());
                        addHabitEventOnline(habitEvent, habit, ctx);
                        hec = cQueue.poll();
                    }

                    else if (hec.getType().equals("delete")) {
                        HabitEvent habitEvent = hec.getEvent();
                        Habit habit = currentUser.getHabitList().getHabit(habitEvent.getHabitName());
                        deleteHabitEventOnline(habitEvent);
                        hec = cQueue.poll();
                    }
                }
            }
        }

        return 0;
    }


}

