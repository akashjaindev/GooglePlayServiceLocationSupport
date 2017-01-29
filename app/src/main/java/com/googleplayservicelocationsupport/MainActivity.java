package com.googleplayservicelocationsupport;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.akashandroid90.googlesupport.location.AppLocationCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;


public class MainActivity extends AppLocationCompatActivity {

    private TextView mOldLocation, mNewLocation;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOldLocation = (TextView) findViewById(R.id.oldlocation);
        mNewLocation = (TextView) findViewById(R.id.newlocation);
        AdView mAdView = (AdView) findViewById(R.id.adView);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startActivity(new Intent(MainActivity.this, FragmentLocationActivity.class));
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        findViewById(R.id.btn_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
                else startActivity(new Intent(MainActivity.this, FragmentLocationActivity.class));
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void newLocation(Location location) {
        mNewLocation.setText(location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void myCurrentLocation(Location currentLocation) {
        mOldLocation.setText(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
    }

    @Override
    public void onActivityResultError(int resultCode, ServiceError serviceError, ConnectionResult connectionResult) {
        switch (serviceError) {
            case GOOGLE_PLAY_SERVICE_ERROR:
                break;
            case LOCATION_SETTING_ERROR:
                break;
        }
    }

    @Override
    public void onServiceDialogCancel(DialogInterface dialog, ConnectionResult connectionResult) {
        if (connectionResult != null) {
            if (getGoogleApiAvailability().isUserResolvableError(connectionResult.getErrorCode()))
                showServiceErrorDialog(connectionResult);
            else
                Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.play_store:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.googleplayservicelocationsupport"));
                startActivity(intent);
                return true;
            case R.id.other_apps:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:AkashJain"));
                startActivity(intent);
                return true;
            case R.id.github:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/akashandroid90/GooglePlayServiceLocationSupport"));
                startActivity(intent);
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Material Icon");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=com.googleplayservicelocationsupport");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are you sure want to exit from app?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Rate us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.googleplayservicelocationsupport"));
                startActivity(intent);
            }
        }).create().show();
    }
}
