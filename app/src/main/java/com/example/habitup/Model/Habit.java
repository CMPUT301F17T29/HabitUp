package com.example.habitup.Model;


import java.time.LocalDate;
import java.util.ArrayList;

public class Habit {

    // Members
    private String name;
    private Boolean schedule[];
    private String reason;
    private String attribute;
    private HabitEventList habitEvents;

    /**
     * Habit constructor
     *
     * @param name String for the Habit name
     * @param reason String for the reason of Habit
     * @param attribute Attributes object to identify the associated attribute with the Habit
     *
     * @author @alido8592
     */

    /**
     * Empty constructor
     */
    public Habit() {

    }

    public Habit(String name, String reason, String attribute, LocalDate startDate, Boolean schedule[])
            throws IllegalArgumentException, IllegalStateException {

        //TODO:
        // limits for name length and unique name and reason length and minimum 1 day scheduled
        // String attribute must be from Attributes

        this.name = name;
        this.schedule = schedule;
        this.reason = reason;
        this.attribute = attribute;
        habitEvents = new HabitEventList();

    }

    /**
     * getHabitName
     * Gets the String of the Habit's name
     * @return String
     */
    public String getHabitName() {return this.name;}

    /**
     * getHabitSchedule
     * Forms a String of days of when the Habit is scheduled
     * @return String
     */
    public Boolean[] getHabitSchedule() {

       return schedule;
    }

    /**
     * getHabitReason
     * Gets the Habit's reason as a String
     * @return String
     */
    public String getHabitReason() {return this.reason;}

    /**
     * getHabitAttribute
     * Gets the associated attribute of the Habit
     * @return String
     */
    public String getHabitAttribute() {return this.attribute;}

    /**
     * getHabitEvents
     * Gets the associated ArrayList of HabitEvent objects for the Habit
     * @return ArrayList<HabitEvent>
     */
    public HabitEventList getHabitEvents() {return this.habitEvents;}

    /**
     * setHabitName
     * Sets the Habit's name into String name
     * @param name String for the desired new name
     * @throws IllegalArgumentException
     */
    public void setHabitName(String name) throws IllegalArgumentException{

        this.name = name;
        //TODO: Implement unique Habit name and limit length
    }

    /**
     * setSchedule
     * Changes the schedule accordingly by changing the Boolean values
     */
    public void setSchedule(Boolean schedule[]) throws IllegalStateException{

        //TODO: minimum 1 day scheduled

        this.schedule = schedule;
    }

    /**
     * setReason
     * Sets the Habit's reason as the provided String
     * @param reason String to represent the new desired reason
     * @throws IllegalArgumentException
     */
    public void setReason(String reason) throws IllegalArgumentException {

        this.reason = reason;
        //TODO: Implement reason length limit
    }

    /**
     * setAttribute
     * Sets the Habit's attribute into the provided
     * @param attribute Attributes object to be associated to the Habit
     * @throws IllegalArgumentException
     */
    public void setAttribute(String attribute) throws IllegalArgumentException{
        //TODO: String attribute must be from Attributes.attributeNames

        this.attribute = attribute;
    }

    public void updateSchedule() {
        //TODO: implement
    }

    /**
     * addHabitEvent
     * Adds a new HabitEvent into the Habit's ArrayList
     * @param habitEvent HabitEvent to be added into the Habit
     */
    public void addHabitEvent(HabitEvent habitEvent){

        this.habitEvents.add(habitEvent);
    }

    /**
     * removeHabitEvent
     * Removes the specified HabitEvent from the Habit's ArrayList
     * @param habitEvent HabitEvent to be removed
     */
    public void removeHabitEvent(HabitEvent habitEvent){

        this.habitEvents.remove(habitEvent);
    }

    /**
     * Returns True if this Habit is set for today in its schedule.
     * @return Boolean
     */
    public Boolean isTodayHabit() {
        return schedule[LocalDate.now().getDayOfWeek().getValue()];
    }
}
