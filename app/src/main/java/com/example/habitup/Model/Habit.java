package com.example.habitup.Model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Habit {

    // Members
    private int uid;
    private String name;
    private Boolean[] schedule = new Boolean[8];
    private String reason;
    private String attribute;
    private HabitEventList habitEvents;
    private LocalDate startDate;

    /**
     * Habit constructor
     *
     * @param uid int for id to associate habits and habitevents with user
     * @param name String for the Habit name
     * @param reason String for the reason of Habit
     * @param attribute Attributes object to identify the associated attribute with the Habit
     * @param startDate LocalDate for startDate to associate with Habit
     * @param schedule Boolean[8] for the schedule regarding the days the Habit is active
     *
     * @author @alido8592
     */

    /**
     * Empty constructor
     */
    public Habit() {

    }

    public Habit(int uid, String name, String reason, String attribute, LocalDate startDate, Boolean[] schedule)
            throws IllegalArgumentException, IllegalStateException {

        //TODO:
        // unique name (HabitList?) or controller

        setUID(uid);
        setHabitName(name);
        setReason(reason);
        setAttribute(attribute);
        habitEvents = new HabitEventList(); //temporary
        this.startDate = startDate;
        setSchedule(schedule);

    }

    /**
     * isLegalNameLength
     * Checks for Habit name length between 1 - 20
     * @param name
     * @return Boolean
     */
    public Boolean isLegalNameLength(String name){
        return !((name.trim().length()==0)||(name.trim().length()>20));
    }

    /**
     * isLegalReasonLength
     * Checks for Habit reason length between 1 - 30
     * @param reason
     * @return Boolean
     */
    public Boolean isLegalReasonLength(String reason){
        return !((reason.trim().length()==0)||(name.trim().length()>30));
    }

    /**
     * isLegalSchedule
     * Checks for Habit schedule containing at least 1 day schedule for the Habit
     * @param schedule Boolean[8]
     * @return Boolean
     */
    public Boolean isLegalSchedule(Boolean[] schedule){
        int trueCount = 0;
        for (Boolean s : schedule) if (s){trueCount++;}
        if (trueCount>=1){return Boolean.TRUE;}
        else return Boolean.FALSE;
    }

    /**
     * isLegalAttribute
     * Checks for Habit attribute is from the established list of attributes
     * @param attribute
     * @return
     */
    public Boolean isLegalAttribute(String attribute){
        //temporary as unsure of using Attributes.contains(attribute)
        Attributes attributesList = new Attributes();
        /*ArrayList<String> attributesList = new ArrayList<>(4);
        attributesList.set(0, "Mental");
        attributesList.set(1, "Physical");
        attributesList.set(2, "Social");
        attributesList.set(3, "Discipline");*/
        if(attributesList.contains(attribute)) return true;
        else return false;
    }

    /**
     * getUID
     * Gets the int uid of habit
     * @return int
     */
    public int getUID() {return this.uid;}

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
    public Boolean[] getHabitSchedule() {return this.schedule;}

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
     * getStartDate
     * Gets the Habit's start date
     * @return Date
     */
    public LocalDate getStartDate() {return this.startDate;}

    /**
     * setUID
     * sets the Habit's uid into the input uid
     * @param uid int uid associated with user
     */
    public void setUID(int uid) {this.uid = uid;}

    /**
     * setHabitName
     * Sets the Habit's name into String name
     * @param name String for the desired new name
     * @throws IllegalArgumentException
     */
    public void setHabitName(String name) throws IllegalArgumentException{

        if(isLegalNameLength(name)){
            this.name = name;
        }
        else{
            throw new IllegalArgumentException("Error: Name length has to be within " +
                    "1 - 20 characters");
        }
        //TODO: Implement unique Habit name in HabitList? or controller
    }

    /**
     * setSchedule
     * Changes the schedule accordingly with input schedule
     * @param schedule Boolean[8]
     */
    public void setSchedule(Boolean[] schedule) throws IllegalStateException{
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

        if (isLegalReasonLength(reason)){
            this.reason = reason;}
        else{
            throw new IllegalArgumentException("Error: Reason length has to be between " +
                    "1 - 30 characters.");
        }
    }

    /**
     * setAttribute
     * Sets the Habit's attribute into the provided
     * @param attribute Attributes object to be associated to the Habit
     * @throws IllegalArgumentException
     */
    public void setAttribute(String attribute) throws IllegalArgumentException{
        if(isLegalAttribute(attribute)){
            this.attribute = attribute;}
        else{
            throw new IllegalArgumentException("Error: Attribute is not from " +
                    "the provided attributes.");
        }
    }

    public void updateSchedule() {
        //TODO: implement
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
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
     * Returns True if this Habit is set for today in its schedule
     * @return Boolean
     */
    public Boolean isTodayHabit() {
        return schedule[LocalDate.now().getDayOfWeek().getValue()];
    }
}
