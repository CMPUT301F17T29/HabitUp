package com.example.habitup.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileActivity extends AppCompatActivity {

    private UserAccount currentUser;
    private CircleImageView profilePic;
    TextView userLoginName;
    TextView userFullName;
    Button saveButton;
    private boolean changedPhoto = false;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Get the user
        currentUser = HabitUpApplication.getCurrentUser();

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get current user's username and set it
        userLoginName = (TextView) findViewById(R.id.profile_username);
        userLoginName.setText(currentUser.getUsername());

        // Get current user's full name and set it in edit field hint
        userFullName = (EditText) findViewById(R.id.edit_full_name);
        userFullName.setText(currentUser.getRealname());

        // Get image
        profilePic = (CircleImageView) findViewById(R.id.edit_pic);
        if (currentUser.getPhoto() != null) {
            profilePic.setImageBitmap(currentUser.getPhoto());
        }

        // Allow user to take photo when clicking the photo icon
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoIntent, REQUEST_CODE);
                }
            }
        });

        // Save button functionality
        saveButton = (Button) findViewById(R.id.save_profile);

        saveButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Activated when save button is clicked
             * @param v View
             */
            public void onClick(View v) {
                setResult(RESULT_OK);

                // Get all the values
                String username = userLoginName.getText().toString();
                String fullname = userFullName.getText().toString();

                // Get image, if there is one
                Bitmap photo = null;
                if ( ((ImageView) profilePic).getDrawable() != null) {
                    photo = ((BitmapDrawable) ((ImageView) profilePic).getDrawable()).getBitmap();
                }

                // Update the values
                Boolean profileOK = Boolean.TRUE;

                try {
                    currentUser.setUsername(username);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    profileOK = Boolean.FALSE;
                }

                try {
                    currentUser.setRealname(fullname);
                } catch (IllegalArgumentException e) {
                    // do stuff
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    profileOK = Boolean.FALSE;
                }

                if (photo != null && changedPhoto) {
                    try {
                        currentUser.setPhoto(photo);
                    } catch (IllegalArgumentException e) {
                        // do stuff
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        profileOK = Boolean.FALSE;
                    }
                }

                if (profileOK) {
                    // Pass to the controller
                    if (HabitUpApplication.addUserAccount(currentUser) == 0) {
                        Intent result = new Intent();
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "There was an error updating the User.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

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
            Bitmap imageBitMap = (Bitmap) extras.get("data");

            // TODO: Resize image for to appropriate byte size
            profilePic.setImageBitmap(imageBitMap);
            changedPhoto = true;
        } else {
            changedPhoto = false;
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
