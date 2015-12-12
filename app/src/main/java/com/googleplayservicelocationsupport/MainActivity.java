package com.googleplayservicelocationsupport;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.github.akashandroid90.googlelocationsupport.AppLocationActivity;


public class MainActivity extends AppLocationActivity {

    private TextView mOldLocation, mNewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOldLocation = (TextView) findViewById(R.id.oldlocation);
        mNewLocation = (TextView) findViewById(R.id.newlocation);
    }

    @Override
    protected void newLocation(Location location) {
        mNewLocation.setText(location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    protected void myCurrentLocation(Location currentLocation) {
        mOldLocation.setText(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
    }
}
