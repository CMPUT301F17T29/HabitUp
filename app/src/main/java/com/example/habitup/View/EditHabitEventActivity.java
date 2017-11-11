package com.example.habitup.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditHabitEventActivity extends AppCompatActivity {

    private int action;
    private int uid;
    private int hid;
    private String eid;
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
        uid = intent.getExtras().getInt(ViewHabitEventActivity.HABIT_EVENT_UID);
        hid = intent.getExtras().getInt(ViewHabitEventActivity.HABIT_EVENT_HID);
        eid = intent.getExtras().getString(ViewHabitEventActivity.HABIT_EVENT_EID);

//        ElasticSearchController.GetHabitEvent

        // Get current date (for now)
        // TODO: Get the event's date
        final Calendar cal = Calendar.getInstance(Locale.CANADA);
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        // Get date click button
        dateClicker = (ImageView) findViewById(R.id.event_date_button);
        dateView = (TextView) findViewById(R.id.event_date_text);

        // Set selected date
        setDateString();

        // Set up spinner
        habitSpinner = (Spinner) findViewById(R.id.event_edit_spinner);

        // Set up habit types list
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
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, habitNames);
        habitSpinner.setAdapter(adapter);
        habitSpinner.setOnItemSelectedListener(habitListener);

        // Get location checkbox
        locationSwitch = (Switch) findViewById(R.id.location_switch);

        // Comment text
        commentText = (EditText) findViewById(R.id.event_comment);

        // Get photo icon
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
        saveButton = (Button) findViewById(R.id.save_event);

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
//                result.putExtra("position", position);
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

    /**
     * Includes activity for taking picture
     * @param requestCode the request code for some activity
     * @param resultCode the result code of the activity
     * @param data data from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitMap = (Bitmap) extras.get("data");

            // TODO: Resize image for to appropriate byte size
            image.setImageBitmap(imageBitMap);
            image.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Updates the date string in the date text view
     */
    private void setDateString() {
        String monthName = new DateFormatSymbols().getShortMonths()[month_x];
        String dateString = (monthName) + " " + day_x + ", " + year_x;
        dateView.setText(dateString);
    }

    /**
     * Disables editable fields and specific resources for viewing an event
     */
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

    // Set color of text when habit type is selected
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
}
