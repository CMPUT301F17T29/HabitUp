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
    private ImageView addprofilePic;
    private Bitmap userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //get EditText
        susername = (EditText) findViewById(R.id.signup_username);
        sdisplayname = (EditText) findViewById(R.id.signup_displayname);

        //get button
        Button signUpButton = (Button) findViewById(R.id.signup_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_signup);

        //get image
        addprofilePic = (ImageView) findViewById(R.id.add_profile_pic);

        // change EditText to string
        signUpName = susername.getText().toString();
        realName = sdisplayname.getText().toString();

        // click signup button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    /**
     * check whether this username exist or not
     * @param uname the item in the menu
     * @return true if this username does not exist,false else.
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
            return true;
        } else {
            return false;
        }
    }

    /**
     * Listens for when the user clicks on the back button
     * @param uname the username of the user
     * @param rname the realname of the user
     * @param image the profile photo of the user
     * @return true if create a new user success,false else.
     */

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



