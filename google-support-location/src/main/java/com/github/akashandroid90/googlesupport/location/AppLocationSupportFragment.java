package com.github.akashandroid90.googlesupport.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by akash on 8/12/15.
 */
public abstract class AppLocationSupportFragment extends Fragment
        implements GoogleContextSupportLocation {

    private final int ACCESS_FINE_LOCATION = 250;
    private final int ACCESS_COARSE_LOCATION = 251;
    private final int ACCESS_LOCATION = 252;
    private final int REQUEST_CHECK_SETTINGS = 253;
    private final int ACCESS_FUSED_LOCATION = 254;
    private final int REQUEST_CODE_PLAY_SERVICES = 255;

    protected Context mContext;
    private LocationManager mLocationService;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ConnectionResult mConnectionResult;
    private GoogleApiAvailability mGoogleApiAvailability;

    private boolean enableUpdates;

    /**
     * this method gives GoogleApiAvailability object created
     *
     * @return GoogleApiAvailability
     */
    @Override
    public GoogleApiAvailability getGoogleApiAvailability() {
        return mGoogleApiAvailability;
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

    public LocationManager getLocationService() {
        return mLocationService;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mConnectionResult = null;
        checkLocationEnable();
    }

    @Override
    public void myCurrentLocation(Location currentLocation) {
    }

    @Override
    public void onServiceDialogCancel(DialogInterface dialog, ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showServiceErrorDialog(connectionResult);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onServiceDialogCancel(dialog, mConnectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        newLocation(location);
    }


    protected final void checkLocationEnable() {

        if (enableUpdates && mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest).setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
//                mLocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        requestLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult((Activity) mContext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        mLocationService = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        mLocationRequest = LocationRequest.create();

        Bundle bundle = getArguments();
        if (bundle != null)
            enableUpdates = bundle.getBoolean(AppLocation.REQUEST_UPDATES, true);
        else
            enableUpdates = true;

    }

    /**
     * @param fastInterval time to request update frequently
     */
    @Override
    public void setFastestInterval(long fastInterval) {
        mLocationRequest.setFastestInterval(fastInterval);
        checkLocationEnable();
    }

    /**
     * @param distance used to set displacement for request make
     */
    @Override
    public void setSmallestDisplacement(long distance) {
        mLocationRequest.setSmallestDisplacement(distance);
        checkLocationEnable();
    }

    /**
     * @param interval time in millis for location request
     */
    @Override
    public void setInterval(long interval) {
        mLocationRequest.setInterval(interval);
        checkLocationEnable();
    }

    /**
     * @param priority int value to set location request priority
     */
    @Override
    public void setPriority(int priority) {
        mLocationRequest.setPriority(priority);
        checkLocationEnable();
    }


    @Override
    public void onStart() {
//        if (mLocationSettingsStates != null && mLocationSettingsStates.isLocationUsable()) {
        if (!servicesConnected())
            mGoogleApiClient.connect();
//        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void stopUpdates() {
        if (enableUpdates) {
            enableUpdates = false;
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void startUpdates() {
        if (!enableUpdates) {
            enableUpdates = true;
            requestLocationUpdates();
        }
    }

    private void requestLocationUpdates() {
        if (enableUpdates) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_FUSED_LOCATION);
            else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                requestForCurrentLocation();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_FUSED_LOCATION);
            else {
                requestForCurrentLocation();
            }
        }
    }

    @Override
    public void addApi(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions> value) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(mContext).addApi(LocationServices.API).enableAutoManage((FragmentActivity) mContext, new Random().nextInt(Integer.MAX_VALUE), this).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this);
        if (!value.getName().equalsIgnoreCase(LocationServices.API.getName())) {
            addApi(value);
        }
        mGoogleApiClient = builder.build();
        if (!servicesConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public void addApi(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] value) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(mContext).addApi(LocationServices.API).enableAutoManage((FragmentActivity) mContext, new Random().nextInt(Integer.MAX_VALUE), this).
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
    public boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = mGoogleApiAvailability.isGooglePlayServicesAvailable(mContext);

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
    public Location requestForCurrentLocation() {
        // If Google Play Services is available
        Location currentLocation = null;
        if (servicesConnected()) {
            // Get the current location

            currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (currentLocation == null) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_LOCATION);

            else {
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
            }

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
            else
                currentLocation = mLocationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION);
            else
                currentLocation = mLocationService.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (currentLocation != null)
            myCurrentLocation(currentLocation);
        return currentLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION:
                if ((permissions.length > 0 && permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    requestForCurrentLocation();
                else onRequestPermissionsResultError(requestCode, permissions, grantResults);
                break;
            case ACCESS_FINE_LOCATION:
                if ((permissions.length > 0 && permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    requestForCurrentLocation();
                else onRequestPermissionsResultError(requestCode, permissions, grantResults);
                break;
            case ACCESS_LOCATION:
                if (permissions.length > 0 && ((permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)
                        || permissions[1].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) ||
                        ((permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)
                                || permissions[1].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION))))) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        requestForCurrentLocation();
                    else onRequestPermissionsResultError(requestCode, permissions, grantResults);
                }
            case ACCESS_FUSED_LOCATION:
                if (permissions.length > 0 && ((permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)
                        || permissions[1].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) ||
                        ((permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)
                                || permissions[1].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION))))) {
                    if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        requestForCurrentLocation();
                    } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        requestForCurrentLocation();
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    } else onRequestPermissionsResultError(requestCode, permissions, grantResults);
                }
                break;
        }
    }

    @Override
    public void showServiceErrorDialog(ConnectionResult connectionResult) {
        mConnectionResult = connectionResult;
        if (mGoogleApiAvailability.isUserResolvableError(connectionResult.getErrorCode()))
            mGoogleApiAvailability.getErrorDialog((Activity) mContext, connectionResult.getErrorCode(), REQUEST_CODE_PLAY_SERVICES, this).show();
    }

    @Override
    public void showServiceErrorDialog(ConnectionResult connectionResult, int requestCode) {
        mConnectionResult = connectionResult;
        if (mGoogleApiAvailability.isUserResolvableError(connectionResult.getErrorCode()))
            mGoogleApiAvailability.getErrorDialog((Activity) mContext, connectionResult.getErrorCode(), requestCode, this).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        // All required changes were successfully made
                        if (!mGoogleApiClient.isConnected())
                            mGoogleApiClient.connect();
                        else {
                            try {
                                Thread.sleep(500);
                                requestLocationUpdates();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        onActivityResultError(resultCode, ServiceError.LOCATION_SETTING_ERROR, null);
                        break;
                    default:
                        onActivityResultError(resultCode, ServiceError.LOCATION_SETTING_ERROR, null);
                        break;
                }
                break;
            case REQUEST_CODE_PLAY_SERVICES:
                switch (resultCode) {
                    case RESULT_OK:
                        // All required changes were successfully made
                        if (!mGoogleApiClient.isConnected())
                            mGoogleApiClient.connect();
                        else
                            requestLocationUpdates();
                        break;
                    default:
                        if (mConnectionResult != null)
                            onActivityResultError(resultCode, ServiceError.GOOGLE_PLAY_SERVICE_ERROR, mConnectionResult);
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityResultError(int resultCode, ServiceError serviceError, ConnectionResult connectionResult) {
    }

    @Override
    public void onRequestPermissionsResultError(int requestCode, String[] permissions, int[] grantResults) {
    }
}