package com.example.habitup.View;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * @author Shuyang
 *
 * The login activity
 */

public class LoginActivity extends AppCompatActivity {
    protected EditText loginText;
    private String logInName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // set the logo
        ImageView imageView = (ImageView) findViewById(R.id.title_image);
        imageView.setImageResource(R.drawable.habitup);

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

                } else if (!checkInternet()) {
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


    /**
     * check whether Internet is connected
     */
    private boolean checkInternet(){
        //Taken from: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        //2017-11-11
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();


    }

    /**
     * check whether this username exist or not
     * @param uname the item in the menu
     * @return True if this username exists, otherwise False.
     */
    private boolean checkUserExist(String uname) {
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(uname);
        ArrayList<UserAccount> users = new ArrayList<>();
        try {
            users = getUser.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User from the async object");
        }

        return (users.size() != 0);
    }
}

