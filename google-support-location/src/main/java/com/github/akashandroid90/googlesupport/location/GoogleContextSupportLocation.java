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
public interface GoogleContextSupportLocation extends GoogleSupportLocation, DialogInterface.OnCancelListener {
    void onActivityResultError(int resultCode, ServiceError serviceError, ConnectionResult connectionResult);
    void showServiceErrorDialog(ConnectionResult connectionResult);
    void showServiceErrorDialog(ConnectionResult connectionResult, int requestCode);
    void onServiceDialogCancel(DialogInterface dialog, ConnectionResult connectionResult);
    void onRequestPermissionsResultError(int requestCode, String[] permissions, int[] grantResults);
}
