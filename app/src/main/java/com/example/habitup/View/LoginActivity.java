package com.example.habitup.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

/**
 * This is the the start up launch activity where the user can login into the HabitUp
 * application. If a username does not exist, the user cannot sign in. The user can choose to
 * sign up for a new account from here. A username cannot be empty. Once the user logs in with
 * an existing username, they will be taken to the user's profile.
 *
 * @author Shuyang
 */

public class LoginActivity extends AppCompatActivity {
    protected EditText loginText;
    private String logInName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // get button
        final Button logInButton = (Button) findViewById(R.id.login_button);
        Button signUpButton = (Button) findViewById(R.id.link_signup);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Transfer EditText to string
                logInName = ((EditText) findViewById(R.id.login_edit)).getText().toString().trim();

                if (logInName.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter a username.",
                            Toast.LENGTH_SHORT).show();

                } else if (!HabitUpApplication.isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),
                            "Error: No connection to the internet.",
                            Toast.LENGTH_SHORT).show();

                } else {

                    // We have login input and an internet connection - try to validate user
                    UserAccount loggedInUser = null;
                    ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
                    getUser.execute(logInName);
                    try {
                        loggedInUser = getUser.get().get(0);
                    } catch (Exception e) {
                        Log.i("HabitUpDEBUG", "LogIn - could not get user " + logInName);
                    }

                    // Login Successful - set current user
                    if (loggedInUser != null) {

                        HabitUpApplication.setCurrentUser(loggedInUser);

                        // DEBUG for demo
                        if (loggedInUser.getUsername().equals("BojackHorseman")) {
                            Bitmap d = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.bojack);

                            try {
//                                HabitUpApplication.getCurrentUser().setDemoPhoto(d);
                                HabitUpApplication.getCurrentUser().setPhoto(null);
                            } catch (Exception e) {
                                Log.i("HabitUpDEBUG", "Login: Failed to set debug Photo");
                            }
                            Log.i("HabitUpDEBUG", "Login: Set Bojack Photo");
                        } else if (loggedInUser.getUsername().equals("MrPeanutbutter")) {
                            Bitmap d = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.peanut);

                            try {
//                                HabitUpApplication.getCurrentUser().setDemoPhoto(d);
                                HabitUpApplication.getCurrentUser().setPhoto(null);
                            } catch (Exception e) {
                                Log.i("HabitUpDEBUG", "Login: Failed to set debug Photo");
                            }
                            Log.i("HabitUpDEBUG", "Login: Set Peanutbutter Photo");
                        }
                        // DEBUG for demo

                        Toast.makeText(getApplicationContext(), logInName + " is now logged in.",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    // Login unsuccessful
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error: username not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    // Override soft back button in Login screen
    @Override
    public void onBackPressed() { }
}

