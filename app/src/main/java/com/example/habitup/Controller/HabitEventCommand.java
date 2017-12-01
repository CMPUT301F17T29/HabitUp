package com.example.habitup.Controller;

import com.example.habitup.Model.HabitEvent;

/**
 * Created by Ty on 2017-11-26.
 */

public class HabitEventCommand {

    private String type;
    private HabitEvent event;

    public HabitEventCommand(String type, HabitEvent event){
        setType(type);
        setEvent(event);
    }

    public void setType(String type){
        this.type = type;
    }

    public void setEvent(HabitEvent event){ this.event = event; }

    public String getType(){ return this.type; }

    public HabitEvent getEvent(){ return this.event; }

}
