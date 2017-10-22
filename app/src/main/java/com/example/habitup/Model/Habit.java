package com.example.habitup.Model;


import java.util.ArrayList;

public class Habit {

    // Members
    private String name;
    private ArrayList<Boolean> schedule;
    private String reason;
    private Attributes attribute;
    private ArrayList<HabitEvent> habitEvents;

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

    public Habit(String name, Boolean Mon, Boolean Tue, Boolean Wed, Boolean Thu,
                 Boolean Fri, Boolean Sat, Boolean Sun, String reason, Attributes attribute)
            throws IllegalArgumentException, IllegalStateException {

        //TODO: limits for name length and reason length

        this.name = name;
        this.schedule = new ArrayList<Boolean>(7);
        this.schedule.set(0, Mon);
        this.schedule.set(1, Tue);
        this.schedule.set(2, Wed);
        this.schedule.set(3, Thu);
        this.schedule.set(4, Fri);
        this.schedule.set(5, Sat);
        this.schedule.set(6, Sun);
        this.reason = reason;
        this.attribute = attribute;
        habitEvents = new ArrayList<HabitEvent>();

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

        ArrayList<String> days = new ArrayList<String>;
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");

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
     * @return Attributes
     */

    public Attributes getHabitAttribute() {return this.attribute;}

    /**
     * getHabitEvents
     * Gets the associated ArrayList of HabitEvent objects for the Habit
     * @return ArrayList<HabitEvent>
     */

    public ArrayList<HabitEvent> getHabitEvents() {return this.habitEvents;}

    public void setHabitName(String name) throws IllegalArgumentException{

        this.name = name;
        //TODO: Implement prevention of using existing Habit name and limit length
    }

    public void setSchedule(Boolean Mon, Boolean Tue, Boolean Wed, Boolean Thu,
                            Boolean Fri, Boolean Sat, Boolean Sun) {

        this.schedule.set(0, Mon);
        this.schedule.set(1, Tue);
        this.schedule.set(2, Wed);
        this.schedule.set(3, Thu);
        this.schedule.set(4, Fri);
        this.schedule.set(5, Sat);
        this.schedule.set(6, Sun);
    }

    public void setReason(String reason) throws IllegalArgumentException,
            IllegalStateException{

        this.reason = reason;
        //TODO: Implement reason length limit
    }

    public void setAttribute(Attributes attribute) throws IllegalArgumentException{

        this.attribute = attribute;
    }





}
