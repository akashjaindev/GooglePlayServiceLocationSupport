package com.googleplayservicelocationsupport;

import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.github.akashandroid90.googlesupport.location.AppLocationService;

import java.lang.ref.WeakReference;

/**
 * Created by Akash Jain on 22-Jan-17.
 */

public class LocationService extends AppLocationService {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder<>(this);
    }

    @Override
    public void myCurrentLocation(Location currentLocation) {
        super.myCurrentLocation(currentLocation);
        Toast.makeText(this,"lat:"+currentLocation.getLatitude()+" lng"+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void newLocation(Location location) {
        super.newLocation(location);
        Toast.makeText(this,"lat:"+location.getLatitude()+" lng"+location.getLongitude(),Toast.LENGTH_SHORT).show();
    }

    public class LocalBinder<S> extends Binder {
        private final WeakReference<S> mService;

        public LocalBinder(final S service) {
            mService = new WeakReference<S>(service);
        }

        public S getService() {
            return mService.get();
        }

    }
}
