package com.example.habitup.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

/**
 * This is the activity to add a habit. The user must set a habit name and a reason for
 * creating the habit. By default, the start date of the habit is set to the current date.
 * The user may change it to another date. The user must also associate one of the pre-set
 * Attributes to a habit. When a habit is completed, it will allocate points to that Attribute
 * category. The user must select at least 1 day for the habit schedule.
 *
 * @author Shari Barboza
 */
public class AddHabitActivity extends BaseActivity {

    // Habit start date
    private int year_x, month_x, day_x;
    private static final int DIALOG_ID = 0;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

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

                // Validation check for new habit
                try {
                    newHabit.setHabitName(habitName);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    newHabit.setReason(habitReason);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    newHabit.setStartDate(startDate);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    newHabit.setAttribute(attribute);
                } catch (IllegalArgumentException e) {
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
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, datePicker, year_x, month_x, day_x);
        }
        return null;
    }

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

    private void setDateString() {
        String monthName = new DateFormatSymbols().getShortMonths()[month_x];
        String dateString = (monthName) + " " + day_x + ", " + year_x;
        dateView.setText(dateString);
    }

    // The date picker listener for when a user selects a date
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

    // Listens for a click inside the Attributes spinner and sets the selected row to
    // its corresponding Attribute color
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
}