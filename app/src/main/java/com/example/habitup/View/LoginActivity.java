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

        //set the logo
        ImageView imageView = (ImageView) findViewById(R.id.title_image);
        imageView.setImageResource(R.drawable.habitup);

        //get button
        Button logInButton = (Button) findViewById(R.id.login_button);
        Button signUpButton = (Button) findViewById(R.id.link_signup);

        //get login EditText username
        loginText = (EditText) findViewById(R.id.login_edit);

        //Transfer EditText to string
        logInName = loginText.getText().toString();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUserExist(logInName) && checkInternet()){
                    Toast.makeText(getApplicationContext(),
                            logInName + " has logged in on this device.",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }else if(!checkInternet()) {
                    Toast.makeText(getApplicationContext(),
                            "Please check your internet connection.",
                        Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please check your username.",
                            Toast.LENGTH_SHORT).show();
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
     * @return true if this user exist,false else.
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
        if (users.size() == 0) {
            return false;
        } else {
            return true;
        }
    }



}

