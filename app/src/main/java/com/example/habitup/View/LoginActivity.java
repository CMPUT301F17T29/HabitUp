package com.example.habitup.View;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.habitup.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ImageView imageView = (ImageView) findViewById(R.id.title_image);
        imageView.setImageResource(R.drawable.habitup);
//
//        Button signInButton = (Button) findViewById(R.id.login_button);


    }
}
