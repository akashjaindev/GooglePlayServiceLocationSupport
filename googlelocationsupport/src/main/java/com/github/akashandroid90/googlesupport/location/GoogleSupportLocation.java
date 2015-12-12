package com.github.akashandroid90.googlesupport.location;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by Akash Jain on 12-Dec-15.
 * this interface is used to provide location
 */
public interface GoogleSupportLocation extends GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {

    int PRIORITY_BALANCED_POWER_ACCURACY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    int PRIORITY_HIGH_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    int PRIORITY_LOW_POWER = LocationRequest.PRIORITY_LOW_POWER;
    int PRIORITY_NO_POWER = LocationRequest.PRIORITY_NO_POWER;

    void newLocation(Location location);

    void myCurrentLocation(Location currentLocation);
}
