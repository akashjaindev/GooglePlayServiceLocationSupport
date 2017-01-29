package com.github.akashandroid90.googlesupport.location;

import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by Akash Jain on 12-Dec-15.
 * this interface is used to provide location
 */
public interface GoogleSupportLocation extends GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    int PRIORITY_BALANCED_POWER_ACCURACY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    int PRIORITY_HIGH_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    int PRIORITY_LOW_POWER = LocationRequest.PRIORITY_LOW_POWER;
    int PRIORITY_NO_POWER = LocationRequest.PRIORITY_NO_POWER;

    void newLocation(Location location);
    void myCurrentLocation(Location currentLocation);
    Location requestForCurrentLocation();
    GoogleApiClient getGoogleApiClient();
    GoogleApiAvailability getGoogleApiAvailability();
    LocationRequest getLocationRequest();
    LocationManager getLocationService();
    void setFastestInterval(long fastInterval);
    void setSmallestDisplacement(long distance);
    void setInterval(long interval);
    void setPriority(int priority);
    void stopUpdates();
    void startUpdates();
    void addApi(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] value);
    void addApi(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions> value);
    boolean servicesConnected();

    enum ServiceError {
        GOOGLE_PLAY_SERVICE_ERROR, LOCATION_SETTING_ERROR
    }
}