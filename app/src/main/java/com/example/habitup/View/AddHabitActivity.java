package com.example.habitup.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habitup.Model.Attributes;
import com.example.habitup.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class AddHabitActivity extends AppCompatActivity {

    // Habit start date
    private int year_x, month_x, day_x;
    private static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get user's edit input
        EditText habitName = (EditText) findViewById(R.id.habit_name);
        EditText habitReason = (EditText) findViewById(R.id.habit_reason);

        // Get current date
        final Calendar cal = Calendar.getInstance(Locale.CANADA);
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        // Get clickable region for calendar on-click listern
        LinearLayout dateLayout = (LinearLayout) findViewById(R.id.date_layout);

        // Set selected date
        setDateString();

        // Set up spinner
        Spinner attrSpinner = (Spinner) findViewById(R.id.habit_attr_spinner);

        // Set up attribute list
        String[] entries = Attributes.getAttributeNames();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, entries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attrSpinner.setAdapter(adapter);

        // Open the date picker dialog
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
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
        TextView dateView = (TextView) findViewById(R.id.date_text);

        String monthName = new DateFormatSymbols().getShortMonths()[month_x];
        String dateString = (monthName) + " " + day_x + ", " + year_x;
        dateView.setText(dateString);
    }
}
