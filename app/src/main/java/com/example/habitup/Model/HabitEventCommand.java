package com.example.habitup.Model;

/**
 * Created by Ty on 2017-11-26.
 */

public class HabitEventCommand {

    private String type;
    private HabitEvent event;
    private String habitName;

    public HabitEventCommand(String type, HabitEvent event, String habit){
        setType(type);
        setEvent(event);
        setHabitName(habit);
    }

    public void setType(String type){
        this.type = type;
    }

    public void setEvent(HabitEvent event){ this.event = event; }

    public void setHabitName(String name){
        this.habitName = name;
    }

    public String getType(){ return this.type; }

    public HabitEvent getEvent(){ return this.event; }

    public String getHabitName(){
        return this.habitName;
    }

}
