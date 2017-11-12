package com.example.habitup.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * @author Shuyang
 *
 * The signup activity
 */
public class SignUpActivity extends AppCompatActivity {
    protected EditText susername;
    protected EditText sdisplayname;
    private String signUpName;
    private String realName;
    private static final int REQUEST_CODE = 1;
    private static final int maxByteCount = 65536;
    private Button addprofilePic;
    private Bitmap userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // get EditText
        susername = (EditText) findViewById(R.id.signup_username);
        sdisplayname = (EditText) findViewById(R.id.signup_displayname);

        // get image
        addprofilePic = (Button) findViewById(R.id.add_profile_pic);

        // get button
        Button signUpButton = (Button) findViewById(R.id.signup_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_signup);

        // click signup button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // change EditText to string
                signUpName = susername.getText().toString().trim();
                realName = sdisplayname.getText().toString().trim();

                if (signUpName.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: Please enter a username.",
                            Toast.LENGTH_SHORT).show();
                } else if (realName.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: Please enter a name.",
                            Toast.LENGTH_SHORT).show();
                } else if (!HabitUpApplication.isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),
                            "Error: You are not connected to the internet.",
                            Toast.LENGTH_SHORT).show();
                } else {

                    // Inputs are valid; see if username already exists
                    ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
                    getUser.execute(signUpName);
                    ArrayList<UserAccount> result = null;
                    try {
                        result = getUser.get();
                    } catch (Exception e) {
                        Log.i("HabitUpDEBUG", "No user match found from ES in SignUp");
                    }

                    if (result.size() == 0) {
                        Log.i("HabitUpDEBUG", "UNIQUE USERNAME OK");
                        try {
                            UserAccount newUser = new UserAccount(signUpName, realName, userimage);
                            HabitUpApplication.addUserAccount(newUser);
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } catch (IllegalArgumentException e) {
                            Toast.makeText(getApplicationContext(),
                                    e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error: username already exists.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // click add profile photo
        addprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoIntent, REQUEST_CODE);
                }
            }
        });

        // click cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
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
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            while(true){
                if (stream.toByteArray().length >= maxByteCount) {
                    resizeImage(imageBitMap);
                }
                else{
                    break;
                }
            }
            userimage = imageBitMap;

        }
    }

    /**
     * Resize the image in order to satisfy requirement
     * @param bp the image need to resize
     */

    private void resizeImage(Bitmap bp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
    }
}



