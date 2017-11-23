package com.example.habitup.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This is the activity for editing a habit. The user must ensure a non-empty habit name and a
 * reason are set. The user can also change the start date and the associated attribute. The user
 * can add or remove more days for the habit's schedule, but must have at least 1 day selected.
 *
 * @author Shari Barboza
 */
public class EditHabitActivity extends AppCompatActivity {

    // Core functionality
    private int hid;
    private int action;
    private Habit habit;

    // Habit Edit Fields
    private EditText editName;
    private EditText editReason;

    // Habit start date
    private int year_x, month_x, day_x;
    private static final int DIALOG_ID = 0;
    private ImageView dateLayout;
    private TextView dateView;

    // Attributes spinner
    private Spinner attrSpinner;

    // Schedule Check Boxes
    private CheckBox checkBoxMon;
    private CheckBox checkBoxTue;
    private CheckBox checkBoxWed;
    private CheckBox checkBoxThu;
    private CheckBox checkBoxFri;
    private CheckBox checkBoxSat;
    private CheckBox checkBoxSun;

    // Change check
    String originalName;

    // Save button
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get the habit from intent
        Intent intent = getIntent();
        hid = intent.getExtras().getInt(ViewHabitActivity.INTENT_HID);
        action = intent.getExtras().getInt(ViewHabitActivity.INTENT_ACTION);

        // Get habit object from controller and set editable fields
        UserAccount currentUser = HabitUpApplication.getCurrentUser();
        String habitName = intent.getExtras().getString("HABIT NAME");
        habit = currentUser.getHabitList().getHabit(habitName);

        // Set habit name
        editName = (EditText) findViewById(R.id.habit_name);
        editName.setText(habit.getHabitName());
        originalName = habit.getHabitName();

        // Set habit reason
        editReason = (EditText) findViewById(R.id.habit_reason);
        editReason.setText(habit.getHabitReason());

        // Get habit's date
        year_x = habit.getStartDate().getYear();
        month_x = habit.getStartDate().getMonthValue() - 1;
        day_x = habit.getStartDate().getDayOfMonth();

        // Get clickable region for calendar on-click listener
        dateLayout = (ImageView) findViewById(R.id.habit_date_button);
        dateView = (TextView) findViewById(R.id.date_text);

        // Set selected date
        setDateString();

        // Set up spinner
        attrSpinner = (Spinner) findViewById(R.id.habit_attr_spinner);

        // Set up attribute list
        int entryIndex = 0;
        String[] entries = Attributes.getAttributeNames();
        AttributeAdapter adapter = new AttributeAdapter(this, R.layout.spinner_item, entries);
        attrSpinner.setAdapter(adapter);
        attrSpinner.setOnItemSelectedListener(attributeListener);

        // Set spinner attribute to the habit's attribute
        for (int i = 0; i < entries.length; ++i) {
            if (entries[i].equals(habit.getHabitAttribute())) {
                entryIndex = i;
            }
        }

        attrSpinner.setSelection(entryIndex);

        // Check the boxes according to the habit's schedule
        checkBoxMon = (CheckBox) findViewById(R.id.monday);
        checkBoxTue = (CheckBox) findViewById(R.id.tuesday);
        checkBoxWed = (CheckBox) findViewById(R.id.wednesday);
        checkBoxThu = (CheckBox) findViewById(R.id.thursday);
        checkBoxFri = (CheckBox) findViewById(R.id.friday);
        checkBoxSat = (CheckBox) findViewById(R.id.saturday);
        checkBoxSun = (CheckBox) findViewById(R.id.sunday);

        boolean[] schedule = habit.getHabitSchedule();

        checkBoxMon.setChecked(schedule[1]);
        checkBoxTue.setChecked(schedule[2]);
        checkBoxWed.setChecked(schedule[3]);
        checkBoxThu.setChecked(schedule[4]);
        checkBoxFri.setChecked(schedule[5]);
        checkBoxSat.setChecked(schedule[6]);
        checkBoxSun.setChecked(schedule[7]);

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

        // Save button functionality
        saveButton = (Button) findViewById(R.id.save_edit_habit);

        // Disable edit fields if viewing activity
        if (action == ViewHabitActivity.VIEW_HABIT) {
            viewMode();
        }

        // When save button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Activated when save button is clicked
             * @param v View
             */
            public void onClick(View v) {
                setResult(RESULT_OK);

                // Get all the values
                String habitName = editName.getText().toString();
                String habitReason = editReason.getText().toString();
                String dateString = dateView.getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
                LocalDate startDate = LocalDate.parse(dateString, formatter);
                String attribute = attrSpinner.getSelectedItem().toString();
                boolean schedule[] = new boolean[8];
                schedule[0] = Boolean.FALSE;
                schedule[1] = checkBoxMon.isChecked();
                schedule[2] = checkBoxTue.isChecked();
                schedule[3] = checkBoxWed.isChecked();
                schedule[4] = checkBoxThu.isChecked();
                schedule[5] = checkBoxFri.isChecked();
                schedule[6] = checkBoxSat.isChecked();
                schedule[7] = checkBoxSun.isChecked();

                Boolean habitOK = Boolean.TRUE;

                // Validation for habit
                try {
                    habit.setHabitName(habitName);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    habit.setReason(habitReason);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    habit.setStartDate(startDate);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    habit.setAttribute(attribute);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                try {
                    habit.setSchedule(schedule);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    habitOK = Boolean.FALSE;
                }

                if (habitOK) {
                    // Pass to the controller
                    try {
                        HabitUpController.editHabit(habit, originalName.equals(habit.getHabitName()));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent result = new Intent();
                result.putExtra(ViewHabitActivity.INTENT_HID, hid);
                setResult(Activity.RESULT_CANCELED, result);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, datePicker, year_x, month_x, day_x);
        }
        return null;
    }

    //
    private void setDateString() {
        String monthName = new DateFormatSymbols().getShortMonths()[month_x];
        String dateString = (monthName) + " " + day_x + ", " + year_x;
        dateView.setText(dateString);
    }


    // Disables editable fields and specific resources for viewing a habit
    private void viewMode() {
        // Disable name edit
        editName.setFocusable(false);
        editName.setBackgroundResource(0);
        editName.setPadding(0, 0, 0, 0);

        // Disable reason edit
        editReason.setFocusable(false);
        editReason.setBackgroundResource(0);
        editReason.setPadding(0, 0, 0, 0);

        // Disable date edit
        dateLayout.setVisibility(View.INVISIBLE);
        dateLayout.setOnClickListener(null);
        dateView.setBackgroundResource(0);
        dateView.setPadding(0, 0, 0, 0);
        dateView.setOnClickListener(null);

        // Disable attribute spinner
        attrSpinner.setBackgroundResource(0);
        attrSpinner.setBackgroundColor(getResources().getColor(android.R.color.white));
        attrSpinner.setPadding(0, 0, 0, 0);
        attrSpinner.setEnabled(false);

        // Disable checkboxes in schedule
        checkBoxMon.setClickable(false);
        checkBoxTue.setClickable(false);
        checkBoxWed.setClickable(false);
        checkBoxThu.setClickable(false);
        checkBoxFri.setClickable(false);
        checkBoxSat.setClickable(false);
        checkBoxSun.setClickable(false);
        
        // Hide save button
        saveButton.setVisibility(View.INVISIBLE);
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

    // Sets selected attribute color and padding in the attributes spinner
    private AdapterView.OnItemSelectedListener attributeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String[] names = Attributes.getAttributeNames();
            String attributeName = names[position];
            String color = Attributes.getColour(attributeName);

            TextView text = view.findViewById(R.id.spinner_text);
            text.setTextColor(Color.parseColor(color));

            if (action == 2) {
                text.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
