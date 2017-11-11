package com.example.habitup.Model;


import java.time.LocalDate;

public class Habit {

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
     * Habit constructor
     *
     * @param uid int for id to associate habits and habitevents with user
     * @param name String for the Habit name
     * @param reason String for the reason of Habit
     * @param attribute Attributes object to identify the associated attribute with the Habit
     * @param startDate LocalDate for startDate to associate with Habit
     * @param schedule boolean[8] for the schedule regarding the days the Habit is active
     *
     * @author @alido8592
     */

    /**
     * Empty constructor
     */
    public Habit() {
        this.schedule = new boolean[8];
    }

    public Habit(int uid, String name, String reason, String attribute, LocalDate startDate, boolean[] schedule)
            throws IllegalArgumentException, IllegalStateException {

        //TODO:
        // unique name (HabitList?) or controller
        setUID(uid);
        setUniqueHID();
        setHabitName(name);
        setReason(reason);
        setAttribute(attribute);
        this.startDate = startDate;
        this.schedule = new boolean[8];
        setSchedule(schedule);
    }

    /**
     * isLegalNameLength
     * Checks for Habit name length between 1 - 20
     * @param name
     * @return Boolean
     */
    public Boolean isLegalNameLength(String name){
        return ( (name.trim().length() > 0) && (name.trim().length() <= 20) );
    }

    /**
     * isLegalReasonLength
     * Checks for Habit reason length between 1 - 30
     * @param reason
     * @return Boolean
     */
    public Boolean isLegalReasonLength(String reason){
        return ( (reason.trim().length() > 0) && (reason.trim().length() <= 30) );
    }

    /**
     * isLegalSchedule
     * Checks for Habit schedule containing at least 1 day schedule for the Habit
     * @param schedule boolean[8]
     * @return Boolean
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
     * @param attribute
     * @return
     */
    public Boolean isLegalAttribute(String attribute){

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
     * @return int Associated UID
     */
    public int getUID() { return this.uid; }

    /**
     * getHID
     * Gets the unique identifier of habit
     * @return int habitID
     */
    public int getHID() { return this.hid; }

    /**
     * getHabitName
     * Gets the String of the Habit's name
     * @return String
     */
    public String getHabitName() { return this.name; }

    /**
     * getHabitSchedule
     * Returns habit schedule
     * @return boolean[8]
     */
    public boolean[] getHabitSchedule() {return this.schedule;}

    /**
     * getHabitReason
     * Gets the Habit's reason as a String
     * @return String
     */
    public String getHabitReason() { return this.reason; }

    /**
     * getHabitAttribute
     * Gets the associated attribute of the Habit
     * @return String
     */
    public String getHabitAttribute() { return this.attribute; }

    /**
     * getStartDate
     * Gets the Habit's start date
     * @return Date
     */
    public LocalDate getStartDate() { return this.startDate; }

    /**
     * setUID
     * sets the Habit's uid into the input uid
     * @param uid int uid associated with user
     */
    public void setUID(int uid) { this.uid = uid; }

    public void setUniqueHID() {
        // Do ElasticSearch stuff
        int hid;

        // DEBUG - suppress error
        hid = 0;

        // Set the HID
        setHID(hid);
    }

    /**
     * setHID
     * Sets the Habit's unique identifier
     */
    public void setHID(int hid) { this.hid = hid; }

    /**
     * setHabitName
     * Sets the Habit's name into String name
     * @param name String for the desired new name
     * @throws IllegalArgumentException
     */
    public void setHabitName(String name) throws IllegalArgumentException{

        if (isLegalNameLength(name)) {
            this.name = name;
        } else {
            String errorStr = "Error: Name length must be within 1 - 20 characters";
            throw new IllegalArgumentException(errorStr);
        }
        //TODO: Implement unique Habit name in HabitList? or controller
    }

    /**
     * setSchedule
     * Changes the schedule accordingly with input schedule
     * @param schedule boolean[8]
     */
    public void setSchedule(boolean schedule[]) throws IllegalStateException{
        if (isLegalSchedule(schedule)){
            this.schedule = schedule;}
        else{
            throw new IllegalStateException("Error: Minimum one day scheduled required.");
        }
    }

    /**
     * setReason
     * Sets the Habit's reason as the provided String
     * @param reason String to represent the new desired reason
     * @throws IllegalArgumentException
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
     * @param attribute Attributes object to be associated to the Habit
     * @throws IllegalArgumentException
     */
    public void setAttribute(String attribute) throws IllegalArgumentException{
        if (isLegalAttribute(attribute)) {
            this.attribute = attribute;
        } else {
            String errStr = "Error: Attribute is invalid.";
            throw new IllegalArgumentException(errStr);
        }
    }

    public void updateSchedule() {
        //TODO: implement
    }

    public void setStartDate(LocalDate startDate) throws IllegalArgumentException {
        // TODO: potential error checking, e.g. startDate cannot be after today?
        this.startDate = startDate;
    }

    /**
     * Returns True if this Habit is set for today in its schedule.
     * @return Boolean
     */
    public Boolean isTodayHabit() {
        return schedule[LocalDate.now().getDayOfWeek().getValue()];
    }


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

    public void incrementHabitsDone() { ++this.habitsDone; }
    public void incrementHabitsDoneExtra() { ++this.habitsDoneExtra; }
    public void decrementHabitsDone() { --this.habitsDone; }
    public void decrementHabitsDoneExtra() { --this.habitsDoneExtra; }

    // Use habitsDone / habitsPossible to represent the overall stat (don't want to return
    // a float or something dumb and messy like that - rational representation)
    public int getHabitsDone() { return this.habitsDone; }
    public int getHabitsDoneExtra() { return this.habitsDoneExtra; }
    public int getHabitsPossible() { return this.habitsPossible; }
}
