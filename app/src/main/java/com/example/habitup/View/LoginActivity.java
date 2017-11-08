package com.example.habitup.View;


import android.os.Bundle;

import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.UserAccount;

public class LoginActivity extends BaseActivity {

    HabitUpController hupCtl;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        hupCtl = new HabitUpController();

    }



}
