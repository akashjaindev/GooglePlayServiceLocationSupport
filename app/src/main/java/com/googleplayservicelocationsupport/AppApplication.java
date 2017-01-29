package com.googleplayservicelocationsupport;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by Akash Jain on 28-May-16.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5883105630361246~9038606611");
    }
}
