package com.github.akashandroid90.googlesupport.location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Akash Jain on 23-Nov-16.
 */

public abstract class AppLocationService extends Service implements GoogleSupportLocation {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationService;

    private boolean enableUpdates;
    private GoogleApiAvailability mGoogleApiAvailability;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mLocationService = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationRequest = LocationRequest.create();
        enableUpdates = true;
        if (!servicesConnected()) {
            if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting())
                mGoogleApiClient.connect();
        }
    }

    /**
     * this method gives GoogleApiAvailability object created
     *
     * @return GoogleApiAvailability
     */
    @Override
    public GoogleApiAvailability getGoogleApiAvailability() {
        return mGoogleApiAvailability;
    }

    @Override
    public void onConnected(Bundle bundle) {
        requestForCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.reconnect();
    }

    /**
     * this method gives GoogleApiClient object created
     *
     * @return GoogleApiClient
     */
    @Override
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    /**
     * this method gives LocationRequest object created
     *
     * @return LocationRequest
     */
    @Override
    public LocationRequest getLocationRequest() {
        return mLocationRequest;
    }

    /**
     * this method gives LocationManager object created
     *
     * @return LocationManager
     */
    @Override
    public LocationManager getLocationService() {
        return mLocationService;
    }

    @Override
    public void onLocationChanged(Location location) {
        newLocation(location);
    }

    /**
     * @param fastInterval time to request update frequently
     */
    @Override
    public void setFastestInterval(long fastInterval) {
        mLocationRequest.setFastestInterval(fastInterval);
    }

    /**
     * @param distance used to set displacement for request make
     */
    @Override
    public void setSmallestDisplacement(long distance) {
        mLocationRequest.setSmallestDisplacement(distance);
    }

    /**
     * @param interval time in millis for location request
     */
    @Override
    public void setInterval(long interval) {
        mLocationRequest.setInterval(interval);
    }

    /**
     * @param priority int value to set location request priority
     */
    @Override
    public void setPriority(int priority) {
        mLocationRequest.setPriority(priority);
    }

    @Override
    public boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = mGoogleApiAvailability.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            if (mGoogleApiClient.isConnected())
                return true;
            else
                return false;
        }
        return false;
    }

    @Override
    public void newLocation(Location location) {

    }

    @Override
    public void myCurrentLocation(Location currentLocation) {

    }

    @Override
    public Location requestForCurrentLocation() {
        // If Google Play Services is available
        Location currentLocation = null;
        if (servicesConnected()) {
            // Get the current location
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (currentLocation == null) {

            if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                Location locationGPS = mLocationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationNet = mLocationService.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                long GPSLocationTime = 0;
                if (null != locationGPS) {
                    GPSLocationTime = locationGPS.getTime();
                }

                long NetLocationTime = 0;

                if (null != locationNet) {
                    NetLocationTime = locationNet.getTime();
                }

                if (0 < GPSLocationTime - NetLocationTime) {
                    currentLocation = locationGPS;
                } else {
                    currentLocation = locationNet;
                }
            }else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
                currentLocation = mLocationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                currentLocation = mLocationService.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (currentLocation != null)
            myCurrentLocation(currentLocation);
        return currentLocation;
    }

    public void stopUpdates() {
        if (enableUpdates) {
            enableUpdates = false;
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void requestLocationUpdates() {
        if (enableUpdates) {
            if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                requestForCurrentLocation();
            }
        } else {
            if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                requestForCurrentLocation();
            }
        }
    }

    @Override
    public void startUpdates() {
        if (!enableUpdates) {
            enableUpdates = true;
            requestLocationUpdates();
        }
    }

    @Override
    public void addApi(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] value) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this);
        for (Api<? extends Api.ApiOptions.NotRequiredOptions> options : value) {
            if (!options.getName().equalsIgnoreCase(LocationServices.API.getName())) {
                builder.addApi(options);
            }
        }
        mGoogleApiClient = builder.build();
        if (!servicesConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public void addApi(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions> value) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this);
        if (!value.getName().equalsIgnoreCase(LocationServices.API.getName())) {
            addApi(value);
        }
        mGoogleApiClient = builder.build();
        if (!servicesConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onDestroy();
    }
}