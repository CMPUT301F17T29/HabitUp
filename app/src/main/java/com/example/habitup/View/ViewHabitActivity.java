package com.example.habitup.View;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.habitup.R;

public class ViewHabitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habits);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
