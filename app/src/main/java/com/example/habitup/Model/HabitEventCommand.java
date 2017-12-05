package com.example.habitup.Model;

/**
 * HabitEventCommand is how we handle offline behaviors around HabitEvents: each action is modeled
 * as a Command, which is stored in a queue, and executed when connectivity is available.
 *
 * Created by Ty on 2017-11-26.
 */

public class HabitEventCommand {

    private String type;
    private HabitEvent event;
    private String habitName;

    /**
     * Constructor
     * @param type String
     * @param event HabitEvent
     * @param habit Habit
     */
    public HabitEventCommand(String type, HabitEvent event, String habit){
        setType(type);
        setEvent(event);
        setHabitName(habit);
    }

    /**
     * Set the type of a HabitEventCommand
     * @param type String
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Set the HabitEvent
     * @param event HabitEvent
     */
    public void setEvent(HabitEvent event){ this.event = event; }

    /**
     * Set the Habit Name
     * @param name Name
     */
    public void setHabitName(String name){
        this.habitName = name;
    }

    /**
     * Getter - type
     * @return String type
     */
    public String getType(){ return this.type; }

    /**
     * Getter - HabitEvent
     * @return HabitEvent
     */
    public HabitEvent getEvent(){ return this.event; }

    /**
     * Getter - HabitName
     * @return String habitName
     */
    public String getHabitName(){
        return this.habitName;
    }

}
