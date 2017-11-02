package com.example.habitup.Model;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Habit {

    // Members
    private String name;
    private ArrayList<Boolean> schedule;
    private String reason;
    private String attribute;
    private HabitEventList habitEvents;
    private Date start_date;

    /**
     * Habit constructor
     *
     * @param name String for the Habit name
     * @param Mon Boolean entry signifying if the Habit is schedule for Mon
     * @param Tue Boolean entry signifying if the Habit is schedule for Tue
     * @param Wed Boolean entry signifying if the Habit is schedule for Wed
     * @param Thu Boolean entry signifying if the Habit is schedule for Thu
     * @param Fri Boolean entry signifying if the Habit is schedule for Fri
     * @param Sat Boolean entry signifying if the Habit is schedule for Sat
     * @param Sun Boolean entry signifying if the Habit is schedule for Sun
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

    public Habit(String name, Boolean Sun, Boolean Mon, Boolean Tue, Boolean Wed, Boolean Thu,
                 Boolean Fri, Boolean Sat, String reason, String attribute)
            throws IllegalArgumentException, IllegalStateException {

        //TODO:
        // unique name (HabitList?)
        // String attribute must be from Attributes



        Date today = Calendar.getInstance().getTime();
        setHabitName(name);
        //this.name = name;
        this.schedule = new ArrayList<Boolean>(7);
        setSchedule(Sun,Mon,Tue,Wed,Thu,Fri,Sat);
        /**this.schedule.set(0, Sun);
        this.schedule.set(1, Mon);
        this.schedule.set(2, Tue);
        this.schedule.set(3, Wed);
        this.schedule.set(4, Thu);
        this.schedule.set(5, Fri);
        this.schedule.set(6, Sat);*/
        setReason(reason);
        //this.reason = reason;
        //this.attribute = attribute;
        setAttribute(attribute);
        habitEvents = new HabitEventList();
        this.start_date = today;

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
     * @param schedule
     * @return
     */
    public Boolean isLegalSchedule(ArrayList<Boolean> schedule){
        if (schedule.contains(Boolean.TRUE)) return true;
        else return false;
    }

    /**
     * isLegalAttribute
     * Checks for Habit attribute is from the established list of attributes
     * @param attribute
     * @return
     */
    public Boolean isLegalAttribute(String attribute){
        //temporary as unsure of using Attributes.contains(attribute)
        ArrayList<String> attributesList = new ArrayList<>(4);
        attributesList.set(0, "Mental");
        attributesList.set(1, "Physical");
        attributesList.set(2, "Social");
        attributesList.set(3, "Discipline");
        if(attributesList.contains(attribute)) return true;
        else return false;
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
    public String getHabitSchedule() {

        ArrayList<String> days = new ArrayList<String>();
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");

        StringBuilder scheduleBuilder = new StringBuilder();

        Integer i = 0;

        while (i<7) {

            if (this.schedule.get(i)) {

                scheduleBuilder.append(days.get(i));
                scheduleBuilder.append(" ");
                i++;
            }

            else {i++;}

        }

        String stringSchedule = scheduleBuilder.toString();
        return stringSchedule;
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
     * getStartDate
     * Gets the Habit's start date
     * @return Date
     */
    public Date getStartDate() {return this.start_date;}

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
        //TODO: Implement unique Habit name in HabitList?
    }

    /**
     * setSchedule
     * Changes the schedule accordingly by changing the Boolean values
     * @param Mon Boolean entry signifying if the Habit is schedule for Monday
     * @param Tue Boolean entry signifying if the Habit is schedule for Tuesday
     * @param Wed Boolean entry signifying if the Habit is schedule for Wednesday
     * @param Thu Boolean entry signifying if the Habit is schedule for Thursday
     * @param Fri Boolean entry signifying if the Habit is schedule for Friday
     * @param Sat Boolean entry signifying if the Habit is schedule for Saturday
     * @param Sun Boolean entry signifying if the Habit is schedule for Sunday
     */
    public void setSchedule(Boolean Sun, Boolean Mon, Boolean Tue, Boolean Wed,
                            Boolean Thu, Boolean Fri, Boolean Sat) throws IllegalStateException{

        ArrayList<Boolean> s = new ArrayList<>(7);
        s.set(0, Sun);
        s.set(1, Mon);
        s.set(2, Tue);
        s.set(3, Wed);
        s.set(4, Thu);
        s.set(5, Fri);
        s.set(6, Sat);
        if (isLegalSchedule(s)){

            this.schedule.set(0, Sun);
            this.schedule.set(1, Mon);
            this.schedule.set(2, Tue);
            this.schedule.set(3, Wed);
            this.schedule.set(4, Thu);
            this.schedule.set(5, Fri);
            this.schedule.set(6, Sat);}
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

}
