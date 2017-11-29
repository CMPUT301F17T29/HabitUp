package com.example.habitup.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * This is the activity for adding a habit event. A user must select from one of
 * their existing habits to create a new event. By default, the completion date for the event
 * is set to the current date, but the user may choose another date that is before the
 * current date only. Optionally, when the user turns on the location switch, the user's current
 * location will be associated with the event. The user can also choose to associate an optional
 * photo with the event. The photo must be stored as less than 65,536 bytes.
 *
 * @author Shari Barboza
 */
public class AddHabitEventActivity extends AppCompatActivity {

    // Event completion date
    private TextView dateView;
    private int year_x, month_x, day_x;
    private static final int DIALOG_ID = 1;

    // Clickable image
    private static final int REQUEST_CODE = 1;
    private ImageView image;

    private int habit_pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get current date
        final Calendar cal = Calendar.getInstance(Locale.CANADA);
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        // Get date click button
        ImageView dateClicker = (ImageView) findViewById(R.id.event_date_button);
        dateView = (TextView) findViewById(R.id.event_date_text);

        // Set selected date
        setDateString();

        // Set up spinner
        Spinner habitSpinner = (Spinner) findViewById(R.id.event_habit_spinner);

        // Set up habit types list
        final UserAccount currentUser = HabitUpApplication.getCurrentUser();
        ArrayList<String> habitNames = currentUser.getHabitList().getHabitNames();

        // Get the habit's name if coming from the main profile
        String currentName = null;
        Bundle extras = getIntent().getExtras();
        if (extras.getInt("profile") == 1) {
            habit_pos = extras.getInt("habit_pos");
            currentName = extras.get("habit").toString();
        }

        // Set up list adapter for habit type names
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, habitNames);
        habitSpinner.setAdapter(adapter);

        for (int i = 0; i < habitNames.size(); i++) {
            String habitName = habitNames.get(i);
            if (currentName != null && currentName.equals(habitName)) {
                habitSpinner.setSelection(i);
            }
        }

        // Set up a click listener when an item in the habit spinner is clicked
        habitSpinner.setOnItemSelectedListener(habitListener);

        // Get location checkbox
        Switch locationSwitch = (Switch) findViewById(R.id.location_switch);

        // Get photo icon
        Button imageButton;
        imageButton = (Button) findViewById(R.id.photo_icon);
        image = (ImageView) findViewById(R.id.taken_image);

        // Allow user to take photo when clicking the photo icon
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoIntent, REQUEST_CODE);
                }
            }
        });

        // Open the date picker dialog when clicking calendar button
        dateClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        // Open the date picke dialog when clicking date field
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        // Save button
        Button saveButton = (Button) findViewById(R.id.save_event);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the event habit type
                String habitType = ((Spinner) findViewById(R.id.event_habit_spinner)).getSelectedItem().toString();

                // Get the event comment
                String habitComment = ((TextView) findViewById(R.id.event_comment)).getText().toString();

                // Get the event date
                String completeDateString = ((TextView) findViewById(R.id.event_date_text)).getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
                LocalDate completeDate = LocalDate.parse(completeDateString, formatter);

                // TODO: M5 get location here

                // Get the event photo
                Bitmap photo = null;
                if ( ((ImageView) findViewById(R.id.taken_image)).getDrawable() != null ) {
                    photo = ((BitmapDrawable) ((ImageView) findViewById(R.id.taken_image)).getDrawable()).getBitmap();
                }

                // Create new habit event
                int uid = HabitUpApplication.getCurrentUID();

                Habit eventHabit = currentUser.getHabitList().getHabit(habitType);
                int hid = eventHabit.getHID();
                HabitEvent newEvent = new HabitEvent(uid, hid);
                Boolean eventOK = Boolean.TRUE;
                newEvent.setHabit(hid);

                // Set habit strings
                newEvent.setHabitStrings(eventHabit);

                // Validation for habit event
                try {
                    newEvent.setComment(habitComment);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    eventOK = Boolean.FALSE;
                }

                try {
                    newEvent.setCompletedate(completeDate);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    eventOK = Boolean.FALSE;
                }

                try {
                    newEvent.setPhoto(photo);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    eventOK = Boolean.FALSE;
                }

                if (eventOK) {
                    // Pass to the controller
                    try {
                        HabitUpController.addHabitEvent(newEvent, eventHabit);

                        Intent result = new Intent();
                        result.putExtra("habit_pos", -1);

                        // Check if user levelled up
                        int levelledUp = 0;
                        if (HabitUpController.levelUp()) {
                            levelledUp = 1;
                        }
                        result.putExtra("levelled_up", levelledUp);

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
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, datePicker, year_x, month_x, day_x);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitMap = (Bitmap) extras.get("data");

            image.setImageBitmap(imageBitMap);
            image.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent result = new Intent();
                result.putExtra("habit_pos", habit_pos);
                setResult(Activity.RESULT_CANCELED, result);
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

    // Set color of text when habit type is selected
    private AdapterView.OnItemSelectedListener habitListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int color = ContextCompat.getColor(AddHabitEventActivity.this, R.color.lightgray);
            TextView spinnerText = view.findViewById(R.id.spinner_text);
            spinnerText.setTextColor(color);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

}
