package com.example.habitup.Model;


import java.util.ArrayList;

public class Habit {

    // Members
    private String name;
    private ArrayList<Boolean> schedule;
    private String reason;
    private String attribute;
    private HabitEventList habitEvents;

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
        // limits for name length and unique name and reason length and minimum 1 day scheduled
        // String attribute must be from Attributes

        this.name = name;
        this.schedule = new ArrayList<Boolean>(7);
        this.schedule.set(0, Sun);
        this.schedule.set(1, Mon);
        this.schedule.set(2, Tue);
        this.schedule.set(3, Wed);
        this.schedule.set(4, Thu);
        this.schedule.set(5, Fri);
        this.schedule.set(6, Sat);
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
     * @param Mon Boolean entry signifying if the Habit is schedule for Monday
     * @param Tue Boolean entry signifying if the Habit is schedule for Tuesday
     * @param Wed Boolean entry signifying if the Habit is schedule for Wednesday
     * @param Thu Boolean entry signifying if the Habit is schedule for Thursday
     * @param Fri Boolean entry signifying if the Habit is schedule for Friday
     * @param Sat Boolean entry signifying if the Habit is schedule for Saturday
     * @param Sun Boolean entry signifying if the Habit is schedule for Sunday
     */

    public void setSchedule(Boolean Mon, Boolean Tue, Boolean Wed, Boolean Thu,
                            Boolean Fri, Boolean Sat, Boolean Sun) throws IllegalStateException{

        //TODO: minimum 1 day scheduled

        this.schedule.set(0, Mon);
        this.schedule.set(1, Tue);
        this.schedule.set(2, Wed);
        this.schedule.set(3, Thu);
        this.schedule.set(4, Fri);
        this.schedule.set(5, Sat);
        this.schedule.set(6, Sun);
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

}
