package com.example.habitup.View;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.habitup.Model.Attributes;
import com.example.habitup.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = dateFormat.format(currentDate);

        // Set selected date
        Button dateButton = (Button) findViewById(R.id.date_button);
        dateButton.setText(dateString);

        // Set up spinner
        Spinner attrSpinner = (Spinner) findViewById(R.id.habit_attr_spinner);

        // Set up attribute list
        String[] entries = Attributes.getAttributeNames();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, entries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attrSpinner.setAdapter(adapter);
    }
}
