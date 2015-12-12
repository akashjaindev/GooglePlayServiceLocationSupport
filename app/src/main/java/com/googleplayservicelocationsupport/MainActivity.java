package com.googleplayservicelocationsupport;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.akashandroid90.googlesupport.location.AppLocationActivity;


public class MainActivity extends AppLocationActivity {

    private TextView mOldLocation, mNewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOldLocation = (TextView) findViewById(R.id.oldlocation);
        mNewLocation = (TextView) findViewById(R.id.newlocation);

        findViewById(R.id.btn_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FragmentLocationActivity.class));
            }
        });
    }

    @Override
    public void newLocation(Location location) {
        mNewLocation.setText(location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void myCurrentLocation(Location currentLocation) {
        mOldLocation.setText(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
    }
}
