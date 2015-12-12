package com.googleplayservicelocationsupport;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.akashandroid90.googlelocationsupport.AppLocationFragment;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by Akash Jain on 12-Dec-15.
 */
public class LocationFragment extends AppLocationFragment {
    private TextView mOldLocation, mNewLocation;
    @Override
    protected void newLocation(Location location) {
        mNewLocation.setText(location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    protected void myCurrentLocation(Location currentLocation) {
        mOldLocation.setText(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btn_fragment).setVisibility(View.GONE);
        mOldLocation = (TextView) view.findViewById(R.id.oldlocation);
        mNewLocation = (TextView) view.findViewById(R.id.newlocation);
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            }
        },100);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
