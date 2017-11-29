package com.example.habitup.View;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the activity for editing a habit event. A user can change the associated habit type.
 * The completion date can be changed as long as the date selected is before the current
 * date. The user can also switch on or off the location switch. The event photo can be changed as
 * long as it is under 65,536 bytes.
 *
 * @author Shari Barboza
 */
public class EditHabitEventActivity extends AppCompatActivity {

    private int action;
    private HabitEvent event;

    // Event completion date
    private int year_x, month_x, day_x;
    private static final int DIALOG_ID = 1;

    // Set up date clickables
    ImageView dateClicker;
    TextView dateView;

    // Set up spinner
    Spinner habitSpinner;

    // Set up location switch
    Switch locationSwitch;

    // Set up comment edit text
    EditText commentText;

    // Clickable image
    private static final int REQUEST_CODE = 1;
    private Bitmap imageBitMap;
    private Button imageButton;
    private ImageView image;

    // Save button
    Button saveButton;

    // Save original date
    LocalDate originalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit_event);

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get the habit from intent
        Intent intent = getIntent();
        action = intent.getExtras().getInt(ViewHabitEventActivity.HABIT_EVENT_ACTION);
        String eid = intent.getExtras().getString(ViewHabitEventActivity.HABIT_EVENT_EID);

        ElasticSearchController.GetHabitEventsByEIDTask getHabitEvent = new ElasticSearchController.GetHabitEventsByEIDTask();
        getHabitEvent.execute(eid);

        try {
            event = getHabitEvent.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "EditHabitEvent - couldn't get habit match for eid " + eid);
        }

        // Get the event's date
        year_x = event.getCompletedate().getYear();
        month_x = event.getCompletedate().getMonthValue() - 1;
        day_x = event.getCompletedate().getDayOfMonth();
        originalDate = event.getCompletedate();

        // Get date click button
        dateClicker = (ImageView) findViewById(R.id.event_date_button);
        dateView = (TextView) findViewById(R.id.event_date_text);

        // Set selected date
        setDateString();

        // Set up spinner
        habitSpinner = (Spinner) findViewById(R.id.event_edit_spinner);

        // Set up habit types list
        int entryIndex = 0;
        ArrayList<String> habitNames = new ArrayList<>();
        final HashMap<String, Integer> hids = new HashMap<>();

        // Retrieve habits from current user
        ArrayList<Habit> habitList;
        ElasticSearchController.GetUserHabitsTask getUserHabits = new ElasticSearchController.GetUserHabitsTask();
        getUserHabits.execute(String.valueOf(HabitUpApplication.getCurrentUID()));

        try {
            habitList = getUserHabits.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "EditHabitEvent - couldn't get User Habits");
            habitList = new ArrayList<>();
        }

        // Populate habitNames, hids for dropdown menu and back-translation to Habit
        for (Habit habit : habitList) {
            habitNames.add(habit.getHabitName());
            hids.put(habit.getHabitName(), habit.getHID());
            if (event.getHID() == habit.getHID()) {
                entryIndex = habitList.indexOf(habit);
                Log.i("HabitUpDEBUG", "EditHabitEvent - matched, " + String.valueOf(event.getHID()) + "; index " + String.valueOf(entryIndex));
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, habitNames);
        habitSpinner.setAdapter(adapter);
        habitSpinner.setOnItemSelectedListener(habitListener);

        // Choose the right Habit in the Habit Type dropdown
        habitSpinner.setSelection(entryIndex);

        // Get location checkbox
        locationSwitch = (Switch) findViewById(R.id.location_switch);

        // Comment text
        commentText = (EditText) findViewById(R.id.event_comment);
        commentText.setText(event.getComment());

        // Get photo icon
        imageButton = (Button) findViewById(R.id.photo_icon);
        image = (ImageView) findViewById(R.id.taken_image);
        if (event.getPhoto() != null) {
            image.setImageBitmap(event.getPhoto());
            image.setVisibility(View.VISIBLE);
        }

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

        // Open the date picker dialog when clicking date field
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        // Save button
        saveButton = (Button) findViewById(R.id.save_event);

        // Disable edit fields if viewing activity
        if (action == 2) {
            viewMode();
        }

        // When the save button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Activated when save button is clicked
             * @param v View
             */
            public void onClick(View v) {
                setResult(RESULT_OK);

                // Get all the values
                String eventType = habitSpinner.getSelectedItem().toString();
                String eventComment = commentText.getText().toString();

                String dateString = dateView.getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
                LocalDate completeDate = LocalDate.parse(dateString, formatter);

                Location currentLocation;

                // Get location
                if (locationSwitch.isChecked()) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 100, (float) 1, locationListener);

                        Log.i("HabitUpDEBUG", "CurrentLocation: " + String.valueOf(currentLocation));
                    } else {
                        currentLocation = null;
                        Toast.makeText(EditHabitEventActivity.this, "Unable to get location.", Toast.LENGTH_SHORT).show();
                        locationSwitch.setChecked(false);
                    }
                } else {
                    currentLocation = null;
                }

                Bitmap photo = imageBitMap;

                Boolean eventOK = Boolean.TRUE;

                // Validation for habit event
                try {
                    event.setHabit(hids.get(eventType));
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    eventOK = Boolean.FALSE;
                }

                try {
                    event.setComment(eventComment);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    eventOK = Boolean.FALSE;
                }

                try {
                    event.setCompletedate(completeDate); // TODO: store previous completedate and do checks as req
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    eventOK = Boolean.FALSE;
                }

                if (photo != null) {
                    try {
                        event.setPhoto(photo);
                    } catch (IllegalArgumentException e) {
                        // do stuff
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        eventOK = Boolean.FALSE;
                    }
                }

                if (locationSwitch.isChecked()) {
                    event.setLocation(currentLocation);
                } else {
                    event.setLocation(null);
                }

                if (eventOK) {
                    // Pass to the controller
                    try {
                        HabitUpController.editHabitEvent(event);
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
//                result.putExtra("position", position);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitMap = (Bitmap) extras.get("data");

            image.setImageBitmap(imageBitMap);
            image.setVisibility(View.VISIBLE);
        }
    }

    private void setDateString() {
        String monthName = new DateFormatSymbols().getShortMonths()[month_x];
        String dateString = (monthName) + " " + day_x + ", " + year_x;
        dateView.setText(dateString);
    }

    private void viewMode() {
        // Disable habit type spinner
        habitSpinner.setBackgroundResource(0);
        habitSpinner.setBackgroundColor(getResources().getColor(R.color.white));
        habitSpinner.setEnabled(false);

        // Disable date clickables
        dateClicker.setVisibility(View.INVISIBLE);
        dateClicker.setOnClickListener(null);
        dateView.setBackgroundResource(0);
        dateView.setOnClickListener(null);
        dateView.setPadding(0, 0, 0, 0);

        // Disable location switch
        locationSwitch.setClickable(false);
        locationSwitch.setBackgroundResource(0);
        TextView markerLabel = (TextView) findViewById(R.id.marker_label);
        markerLabel.setBackgroundResource(0);

        // Disable comment field
        commentText.setBackgroundResource(0);
        commentText.setFocusable(false);
        commentText.setPadding(0, 0, 0, 0);

        // Disable photo button
        // TODO: Check whether event has an image, if it does not, remove all photo labels
        RelativeLayout photoLayout = (RelativeLayout) findViewById(R.id.photo_display);
        photoLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        imageButton.setVisibility(View.INVISIBLE);

        // Disable save button
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

    // Set color of text when habit type is selected in habit types spinner
    private AdapterView.OnItemSelectedListener habitListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int color = ContextCompat.getColor(EditHabitEventActivity.this, R.color.lightgray);
            TextView spinnerText = view.findViewById(R.id.spinner_text);
            spinnerText.setTextColor(color);

            if (action == 2) {
                spinnerText.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.i("HabitUpDEBUG", "Location Changed: " + String.valueOf(location));
        }

        public void onStatusChanged(String s, int i, Bundle b) {

        }

        public void onProviderEnabled(String s) {

        }

        public void onProviderDisabled(String s) {

        }
    };
}
