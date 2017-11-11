package com.example.habitup.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habitup.Model.Attributes;
import com.example.habitup.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class EditHabitActivity extends AppCompatActivity {

    private int position;
    private int action;

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
        position = intent.getExtras().getInt("position");
        action = intent.getExtras().getInt("action");

        // TODO: Get habit object from controller and set editable fields

        // Get edit text fields
        editName = (EditText) findViewById(R.id.habit_name);
        editReason = (EditText) findViewById(R.id.habit_reason);

        // TODO: Get habit's date instead of current date
        final Calendar cal = Calendar.getInstance(Locale.CANADA);
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        // Get clickable region for calendar on-click listener
        dateLayout = (ImageView) findViewById(R.id.habit_date_button);
        dateView = (TextView) findViewById(R.id.date_text);

        // Set selected date
        setDateString();

        // Set up spinner
        attrSpinner = (Spinner) findViewById(R.id.habit_attr_spinner);

        // Set up attribute list
        String[] entries = Attributes.getAttributeNames();
        AttributeAdapter adapter = new AttributeAdapter(this, R.layout.attribute_item, entries);
        attrSpinner.setAdapter(adapter);
        attrSpinner.setOnItemSelectedListener(attributeListener);

        attrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] names = Attributes.getAttributeNames();
                String attributeName = names[position];
                String color = Attributes.getColour(attributeName);
                TextView text = view.findViewById(R.id.attribute_text);
                text.setTextColor(Color.parseColor(color));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // TODO: Set spinner attribute to the habit's attribute (get int position from entries)
        // Just setting it to 2 for now so wrong color will currently be set in View Habit
        attrSpinner.setSelection(2);

        // TODO: Check the boxes according to the habit's schedule
        checkBoxMon = (CheckBox) findViewById(R.id.monday);
        checkBoxTue = (CheckBox) findViewById(R.id.tuesday);
        checkBoxWed = (CheckBox) findViewById(R.id.wednesday);
        checkBoxThu = (CheckBox) findViewById(R.id.thursday);
        checkBoxFri = (CheckBox) findViewById(R.id.friday);
        checkBoxSat = (CheckBox) findViewById(R.id.saturday);
        checkBoxSun = (CheckBox) findViewById(R.id.sunday);


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

        // TODO: Implement save button
        saveButton = (Button) findViewById(R.id.save_edit_habit);

        // Disable edit fields if viewing activity
        if (action == 2) {
            viewMode();
        }
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
                Intent result = new Intent();
                result.putExtra("position", position);
                setResult(Activity.RESULT_CANCELED, result);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
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

    private void viewMode() {
        // Disable name edit
        editName.setFocusable(false);
        editName.setBackgroundResource(0);
        editName.setPadding(0,0, 0, 0);

        // Disable reason edit
        editReason.setFocusable(false);
        editReason.setBackgroundResource(0);
        editReason.setPadding(0, 0, 0, 0);

        // Disable date edit
        dateLayout.setVisibility(View.GONE);
        dateLayout.setOnClickListener(null);
        dateView.setBackgroundResource(0);
        dateView.setPadding(0, 0, 0, 0);
        dateView.setOnClickListener(null);

        // Disable attribute spinner
        attrSpinner.setBackgroundResource(0);
        attrSpinner.setBackgroundColor(getResources().getColor(android.R.color.white));
        attrSpinner.setPadding(0, 0, 0, 0);
        View selectedView = attrSpinner.getSelectedView();

        // Disable checkboxes in schedule
        checkBoxMon.setEnabled(false);
        checkBoxTue.setEnabled(false);
        checkBoxWed.setEnabled(false);
        checkBoxThu.setEnabled(false);
        checkBoxFri.setEnabled(false);
        checkBoxSat.setEnabled(false);
        checkBoxSun.setEnabled(false);

        // Hide save button
        saveButton.setVisibility(View.INVISIBLE);
    }

    private AdapterView.OnItemSelectedListener attributeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String[] names = Attributes.getAttributeNames();
            String attributeName = names[position];
            String color = Attributes.getColour(attributeName);
            TextView text = view.findViewById(R.id.attribute_text);
            text.setTextColor(Color.parseColor(color));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
