package com.example.habitup.Controller;


import com.example.habitup.Model.UserAccount;

public class HabitUpController {

    static UserAccount currentUser;

    public HabitUpController() {

        // DEBUG
        currentUser = new UserAccount("gojeffcho", "Jeff Cho", null);
        currentUser.getAttributes().setValue("Mental", 5);
        currentUser.getAttributes().setValue("Discipline", -10);

    }

    static public UserAccount getCurrentUser() { return currentUser; }
    static public int getCurrentUID() { return currentUser.getUID(); }
}
