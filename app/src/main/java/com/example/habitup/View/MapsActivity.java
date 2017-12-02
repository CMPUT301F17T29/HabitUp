package com.example.habitup.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;


/**
 * The Map activity is used for viewing the participant current location,
 * view habit events that have location and view other participant habit event
 * with location that is 5 km near me.
 *
 */

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private CheckBox myCheckbox;
    private CheckBox friendCheckbox;
    private CheckBox highlightCheckbox;

    private Marker myMarker;
    private Location currentLocation;
    private Location friendsLocation;
    boolean myMarkerVisible;
    boolean friendsMarkerVisible;

    ArrayList<HabitEvent> myHabitEventList;
    ArrayList<HabitEvent> friendsEvents;

    LatLng myLatLng;
    LatLng friLatLng;
    LatLng HmyLatLng;
    LatLng HfriLatLng;
    Location myLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setTitle("Map Location Activity");
        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        ElasticSearchController.GetHabitEventsByUidTask getHabitEvents = new ElasticSearchController.GetHabitEventsByUidTask();
        getHabitEvents.execute(HabitUpApplication.getCurrentUIDAsString());

        try {
            myHabitEventList= getHabitEvents.get();
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "MapsActivity - Couldn't get HabitEvents");
        }

        UserAccount currentUser = HabitUpApplication.getCurrentUser();

        // Get friends
        ArrayList<String> friendStringList = currentUser.getFriendsList().getUserList();
        ArrayList<UserAccount> friendList = new ArrayList<>();
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();

        for (String friend : friendStringList) {
            getUser.execute(friend);
            try {
                friendList.add(getUser.get().get(0));
            } catch (Exception e) {
                Log.i("HabitUpDEBUG", "Maps: couldn't get friend " + friend);
            }
        }

        if (friendList.size() != 0) {
            for (UserAccount friend : friendList) {
                UserAccount updatedFriend = HabitUpApplication.getUserAccount(friend.getUsername());

                for (Habit habit : updatedFriend.getHabitList().getHabits()) {
                    HabitEvent recentEvent = updatedFriend.getEventList().getRecentEventFromHabit(habit.getHID());
                    friendsEvents.add(recentEvent);
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                UiSettings mUiSettings = mGoogleMap.getUiSettings();
                mUiSettings.setZoomControlsEnabled(true);
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        myCheckbox = findViewById(R.id.my_habit_history_location);
        friendCheckbox = findViewById(R.id.my_friend_habit_history_location);
        highlightCheckbox = findViewById(R.id.highlight_location);


        myCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mGoogleMap != null) {
                    updateMyMap(isChecked);
                }
            }
        });
        friendCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mGoogleMap != null) {
                    updateFriendMap(isChecked);
                }
            }
        });

        highlightCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mGoogleMap != null) {
                    myMarkerVisible = myCheckbox.isChecked();
                    friendsMarkerVisible = friendCheckbox.isChecked();
                    highlightMap(currentLocation,myMarkerVisible,friendsMarkerVisible);
                }
            }
        });
    }

    private void updateMyMap(boolean visible) {

        if (myHabitEventList != null && myHabitEventList.size() > 0) {
            for (HabitEvent mHabitEvent : myHabitEventList) {
                myLocation = mHabitEvent.getLocation();
                if (myLocation != null) {
                    myLatLng = new LatLng((myLocation.getLatitude()), myLocation.getLongitude());
                    myMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(myLatLng));
                    myMarker.setVisible(visible);

                }
            }
        }
    }
    
    private void updateFriendMap(boolean visible){
        if (friendsEvents != null && friendsEvents.size() > 0) {
            for (HabitEvent fHabitEvent : friendsEvents) {
                friendsLocation = fHabitEvent.getLocation();
                if (friendsLocation != null) {
                    friLatLng = new LatLng((friendsLocation.getLatitude()), friendsLocation.getLongitude());
                    myMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_flag))
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(myLatLng));
                    myMarker.setVisible(visible);

                }
            }
        }
    }

    private void highlightMap(Location currentLocation, boolean myMarkerVisible, boolean friendMarkerVisable){

        if (myHabitEventList != null && myHabitEventList.size() > 0) {
            for (HabitEvent habitEvent : myHabitEventList) {
                myLocation = habitEvent.getLocation();
                if (myLocation != null) {
                    HmyLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    Float distance = currentLocation.distanceTo(myLocation);
                    if (distance < 5000 && myMarkerVisible) {
                        myMarker = mGoogleMap.addMarker(new MarkerOptions()
                                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                .position(HmyLatLng));
                        myMarker.setAlpha((float) 1);
                    }
                }
            }
        }

        if (friendsEvents != null && friendsEvents.size() > 0) {
            for (HabitEvent fHabitEvent : friendsEvents) {
                friendsLocation = fHabitEvent.getLocation();
                if (friendsLocation != null) {
                    HfriLatLng = new LatLng((friendsLocation.getLatitude()), friendsLocation.getLongitude());
                    Float distance = currentLocation.distanceTo(friendsLocation);
                    if (distance < 5000 && friendMarkerVisable) {
                        myMarker = mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_flag))
                                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                .position(HfriLatLng));
                        myMarker.setAlpha((float) 1);
                    }
                }
            }
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }


}