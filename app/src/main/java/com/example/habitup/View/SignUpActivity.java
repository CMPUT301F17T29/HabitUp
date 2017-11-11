package com.example.habitup.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    protected EditText susername;
    protected EditText sdisplayname;
    private String signUpName;
    private String realName;
    private static final int REQUEST_CODE = 1;
    private static final int maxByteCount = 65536;
    private ImageView addprofilePic;
    private Bitmap userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        susername = (EditText) findViewById(R.id.signup_username);
        sdisplayname = (EditText) findViewById(R.id.signup_displayname);
        Button signUpButton = (Button) findViewById(R.id.signup_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_signup);

        addprofilePic = (ImageView) findViewById(R.id.add_profile_pic);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpName = susername.getText().toString();
                realName = sdisplayname.getText().toString();
                if (checkUserExist(signUpName) && !signUpName.equals("")) {
                    if (createNewUser(signUpName,realName,userimage)) {
                        Toast.makeText(getApplicationContext(),
                                "User: " + signUpName + " has been created.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                " User: " + signUpName + " cannot be created.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        addprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoIntent, REQUEST_CODE);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
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
    private void resizeImage(Bitmap bp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
    }

    private boolean checkUserExist(String uname) {
        ElasticSearchController.GetUsersTask getUser = new ElasticSearchController.GetUsersTask();
        getUser.execute(uname);
        ArrayList<UserAccount> users = new ArrayList<>();
        try {
            users = getUser.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User from the async object");
        }
        if (users.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean createNewUser(String uname,String rname, Bitmap image){
        UserAccount newUser = new UserAccount(uname,rname,image);
        try {
            ElasticSearchController.AddUsersTask addUser = new ElasticSearchController.AddUsersTask();
            addUser.execute(newUser);
            return true;
        }catch (Exception e){
            Log.i("Error", "Failed to create the User");
            Toast.makeText(getApplicationContext(),
                    "Can not create user. Internet connection Error",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}



