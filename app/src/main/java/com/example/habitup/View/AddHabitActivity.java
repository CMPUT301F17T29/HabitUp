package com.example.habitup.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.R;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddHabitActivity extends AppCompatActivity {

    // Habit start date
    private int year_x, month_x, day_x;
    private static final int DIALOG_ID = 0;

    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get current date
        final Calendar cal = Calendar.getInstance(Locale.CANADA);
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        // Get clickable region for calendar on-click listern
        ImageView dateLayout = (ImageView) findViewById(R.id.habit_date_button);
        dateView = (TextView) findViewById(R.id.date_text);

        // Set selected date
        setDateString();

        // Set up spinner
        Spinner attrSpinner = (Spinner) findViewById(R.id.habit_attr_spinner);

        // Set up attribute list
        String[] entries = Attributes.getAttributeNames();
        AttributeAdapter adapter = new AttributeAdapter(this, R.layout.spinner_item, entries);
        attrSpinner.setAdapter(adapter);
        attrSpinner.setOnItemSelectedListener(attributeListener);

        // Open the date picker dialog clicking calendar button
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        // Open the date picker dialog clicking date field
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        // Save button
        Button saveButton = (Button) findViewById(R.id.save_habit);

        saveButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Activated when create button is clicked
             * @param v View
             */
            public void onClick(View v) {
                setResult(RESULT_OK);

                // Get Habit name and Reason
                String habitName = ((EditText) findViewById(R.id.habit_name)).getText().toString();
                String habitReason = ((EditText) findViewById(R.id.habit_reason)).getText().toString();

                // Get Habit start date
                String dateString = ((TextView) findViewById(R.id.date_text)).getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
                LocalDate startDate = LocalDate.parse(dateString, formatter);

                Log.i("DATE:", startDate.toString()); // TODO REMOVE

                // Get Habit's associated Attribute
                String attribute = ((Spinner) findViewById(R.id.habit_attr_spinner)).getSelectedItem().toString();

                // Get Schedule array
                boolean schedule[] = new boolean[8];
                CheckBox checkBoxMon = (CheckBox) findViewById(R.id.monday);
                CheckBox checkBoxTue = (CheckBox) findViewById(R.id.tuesday);
                CheckBox checkBoxWed = (CheckBox) findViewById(R.id.wednesday);
                CheckBox checkBoxThu = (CheckBox) findViewById(R.id.thursday);
                CheckBox checkBoxFri = (CheckBox) findViewById(R.id.friday);
                CheckBox checkBoxSat = (CheckBox) findViewById(R.id.saturday);
                CheckBox checkBoxSun = (CheckBox) findViewById(R.id.sunday);

                schedule[0] = Boolean.FALSE;
                schedule[1] = checkBoxMon.isChecked();
                schedule[2] = checkBoxTue.isChecked();
                schedule[3] = checkBoxWed.isChecked();
                schedule[4] = checkBoxThu.isChecked();
                schedule[5] = checkBoxFri.isChecked();
                schedule[6] = checkBoxSat.isChecked();
                schedule[7] = checkBoxSun.isChecked();

                // Create the Habit
                Habit newHabit = new Habit(HabitUpApplication.getCurrentUID());
                Boolean habitOK = Boolean.TRUE;

                try {
                    newHabit.setHabitName(habitName);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    newHabit.setReason(habitReason);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    newHabit.setStartDate(startDate);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    newHabit.setAttribute(attribute);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    newHabit.setSchedule(schedule);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                if (habitOK) {
                    // Pass to the controller
                    try {
                        HabitUpController.addHabit(newHabit);
                        Intent result = new Intent();
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    /**
     * Opening the date picker dialog
     * @param id the dialog id
     * @return the dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, datePicker, year_x, month_x, day_x);
        }
        return null;
    }

    /**
     * Listens for when the user clicks on the back button
     * @param menuItem the item in the menu
     * @return true if the item was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    // Creating the date picker listener for when a user selects a date
    private DatePickerDialog.OnDateSetListener datePicker
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            setDateString();
        }
    };

    /**
     * Updates the date string in the date text view
     */
    private void setDateString() {
        String monthName = new DateFormatSymbols().getShortMonths()[month_x];
        String dateString = (monthName) + " " + day_x + ", " + year_x;
        dateView.setText(dateString);
    }

    private AdapterView.OnItemSelectedListener attributeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String[] names = Attributes.getAttributeNames();
            String attributeName = names[position];
            String color = Attributes.getColour(attributeName);
            TextView text = view.findViewById(R.id.spinner_text);
            text.setTextColor(Color.parseColor(color));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    public void onSave() {

    }
}