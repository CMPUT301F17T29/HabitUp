package com.example.habitup.Model;


import android.util.Log;

import com.example.habitup.Controller.ElasticSearchController;

import java.time.LocalDate;

/**
 * Habit is the object representing a habit type, belonging to a specific user.  It specifies a
 * name, reason, schedule of days of the week it should be completed on, the date to start, and
 * the attribute it is associated with (the attribute it will increase each time the habit is
 * done).
 *
 * The Habit stores some utility data for calculating statistics, namely how many times it was done
 * on schedule, how many times it could have been done if always done on schedule, and how many
 * non-scheduled times it was done.
 *
 * Javadoc last updated 2017-11-13 by @gojeffcho.
 */

public class Habit implements Comparable<Habit> {

    // Members
    private int uid;
    private int hid;
    private String name;
    private boolean[] schedule;
    private String reason;
    private String attribute;
    private LocalDate startDate;

    private LocalDate lastUpdated;
    private int habitsDone;
    private int habitsDoneExtra;
    private int habitsPossible;

    /**
     * Empty constructor - UID is set from ES to the next possible HID.  Habit is then built
     * using setters to set the additional fields.
     * @param uid int (user to whom the Habit belongs)
     */
    public Habit(int uid) {
        this.uid = uid;
        setUniqueHID();
    }

    /**
     * isLegalNameLength
     * Checks for Habit name length between 1 - 20
     * @param name String (Habit name)
     * @return Boolean (True if legal, False if not)
     */
    public Boolean isLegalNameLength (String name) {
        return ( (name.trim().length() > 0) && (name.trim().length() <= 20) );
    }

    /**
     * isLegalReasonLength
     * Checks for Habit reason length between 1 - 30
     * @param reason String (Habit reason)
     * @return Boolean (True if legal, False if not)
     */
    public Boolean isLegalReasonLength(String reason){
        return ( (reason.trim().length() > 0) && (reason.trim().length() <= 30) );
    }

    /**
     * isLegalSchedule
     * Checks for Habit schedule containing at least 1 day scheduled for the Habit
     * @param schedule boolean[8] (0 is unused, 1 for Mon, 2 for Tue...)
     * @return Boolean (True if legal, False if not)
     */
    public Boolean isLegalSchedule(boolean[] schedule){
        int trueCount = 0;
        for (boolean s : schedule) {
            if (s) { trueCount++; }
        }

        return trueCount > 0;
    }

    /**
     * isLegalAttribute
     * Checks for Habit attribute is from the established list of attributes
     * @param attribute String (Name of attribute)
     * @return Boolean (True if legal, False if not)
     */
    public Boolean isLegalAttribute(String attribute) {

        String[] attrNames = Attributes.getAttributeNames();
        for (String name : attrNames) {
            if (name == attribute) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * getUID
     * Gets the uid of habit
     * @return int (UID of associated user)
     */
    public int getUID() { return this.uid; }

    /**
     * getHID
     * Gets the unique identifier of habit
     * @return int (habitID)
     */
    public int getHID() { return this.hid; }

    /**
     * getHabitName
     * Gets the String of the Habit's name
     * @return String (Name of Habit)
     */
    public String getHabitName() { return this.name; }

    /**
     * getHabitSchedule
     * Returns habit schedule
     * @return boolean[8] (0 is unused, 1 for Mon, 2 for Tue...)
     */
    public boolean[] getHabitSchedule() { return this.schedule; }

    /**
     * getHabitReason
     * Gets the Habit's reason as a String
     * @return String (Habit reason)
     */
    public String getHabitReason() { return this.reason; }

    /**
     * getHabitAttribute
     * Gets the associated attribute of the Habit
     * @return String (Attribute that the Habit increases each time when done)
     */
    public String getHabitAttribute() { return this.attribute; }

    /**
     * getStartDate
     * Gets the Habit's start date
     * @return Date (Start date of Habit)
     */
    public LocalDate getStartDate() { return this.startDate; }

    /**
     * Sets the next available HID from ElasticSearch - ensures no duplicate HIDs
     */
    public void setUniqueHID() {
        // Do ElasticSearch stuff
        ElasticSearchController.GetMaxHidTask maxHid = new ElasticSearchController.GetMaxHidTask();
        maxHid.execute();
        int hid;
        try {
            hid = maxHid.get();
        } catch (Exception e) {
            Log.d("HabitUpDEBUG", "ERROR getting MaxHID");
            hid = -1;
        }

        // Set the HID
        setHID(hid);
    }

    /**
     * setHID
     * Sets the Habit's unique identifier
     */
    private void setHID(int hid) { this.hid = hid; }

    /**
     * setHabitName
     * Sets the Habit's name into String name
     * @param name String (Intended Habit name)
     * @throws IllegalArgumentException (if Habit name not legal)
     */
    public void setHabitName(String name) throws IllegalArgumentException {

        if (isLegalNameLength(name)) {
            this.name = name;
        } else {
            String errorStr = "Error: Name length must be within 1 - 20 characters";
            throw new IllegalArgumentException(errorStr);
        }
    }

    /**
     * setSchedule
     * Changes the schedule accordingly with input schedule
     * @param schedule boolean[8] (0 unused, 1 for Mon, 2 for Tue...)
     */
    public void setSchedule(boolean schedule[]) throws IllegalArgumentException {
        if (isLegalSchedule(schedule)){
            this.schedule = schedule;}
        else{
            throw new IllegalArgumentException("Error: Minimum one day scheduled required.");
        }
    }

    /**
     * setReason
     * Sets the Habit's reason as the provided String
     * @param reason String (Intended Habit reason)
     * @throws IllegalArgumentException (if Reason not legal)
     */
    public void setReason(String reason) throws IllegalArgumentException {

        if (isLegalReasonLength(reason)) {
            this.reason = reason;
        } else {
            String errStr = "Error: Reason length has to be between 1 - 30 characters.";
            throw new IllegalArgumentException(errStr);
        }
    }

    /**
     * setAttribute
     * Sets the Habit's attribute into the provided
     * @param attribute String (Attribute associated to the Habit)
     * @throws IllegalArgumentException (if attribute not legal)
     */
    public void setAttribute(String attribute) throws IllegalArgumentException {
        if (isLegalAttribute(attribute)) {
            this.attribute = attribute;
        } else {
            String errStr = "Error: Attribute is invalid.";
            throw new IllegalArgumentException(errStr);
        }
    }

    /**
     * Set start date for the Habit - can be in the past, present, or future
     * @param startDate LocalDate (Intended start date of Habit)
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns True if this Habit is set for today in its schedule.
     * @return Boolean (True if Habit is scheduled for current day)
     */
    public Boolean isTodayHabit() {
        return schedule[LocalDate.now().getDayOfWeek().getValue()];
    }

    /**
     * Utility function for stats calculation - updates the number of possible Habit executions
     * if it were done every day it was scheduled for since its start date.
     */
    public void updateHabitsPossible() {

        // Get today's date - we will compare this to lastCalculated
        LocalDate today = LocalDate.now();
        LocalDate curr = this.lastUpdated;

        // New lastCalculated is today, because we're recalculating.
        this.lastUpdated = today;

        while (curr != today) {

            // Check if curr is one of the days set for the Habit to be done
            // NOTE: DayOfWeek is an enum - Mon == 1, Tue == 2, etc.
            if (this.schedule[curr.getDayOfWeek().getValue()]) {

                // If so, increment habitsPossible
                ++this.habitsPossible;
            }

            // Increment to next day
            // Careful!  Check for month rollover, weird possibilities
            curr.plusDays(1);

        }
    }

    /**
     * Comparator implementation - to sort lexicographically based on name.
     * @param h Habit (to compare to)
     * @return int (comparison)
     */
    public int compareTo(Habit h) {
        return this.name.compareTo(h.getHabitName());
    }

    /**
     * Increment how many times Habit was done on schedule.
     */
    public void incrementHabitsDone() { ++this.habitsDone; }

    /**
     * Increment how any times Habit was done outside of schedule.
     */
    public void incrementHabitsDoneExtra() { ++this.habitsDoneExtra; }

    /**
     * Decrement how many times Habit was done on schedule.
     */
    public void decrementHabitsDone() { --this.habitsDone; }

    /**
     * Decrement how many times Habit was done outside of schedule.
     */
    public void decrementHabitsDoneExtra() { --this.habitsDoneExtra; }

    /**
     * Get how many times Habit was done on schedule.
     * @return int (Number of times Habit was done on schedule)
     */
    public int getHabitsDone() { return this.habitsDone; }

    /**
     * Get how many times Habit was done outside of schedule.
     * @return int (Number of times Habit was done outside of schedule)
     */
    public int getHabitsDoneExtra() { return this.habitsDoneExtra; }

    /**
     * Get how many times Habit could have been done if schedule was followed from start date.
     * @return int (Number of times Habit could have been done if schedule was followed from start date)
     */
    public int getHabitsPossible() { return this.habitsPossible; }

    public int getPercent() {
        int yValue = getHabitsPossible();
        if (yValue != 0) {
            int xValue = getHabitsDone();
            return (xValue / yValue) * 100;
        } else {
            return 0;
        }
    }
}
