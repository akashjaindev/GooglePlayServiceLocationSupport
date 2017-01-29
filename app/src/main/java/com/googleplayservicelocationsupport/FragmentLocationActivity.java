package com.googleplayservicelocationsupport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class FragmentLocationActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_location);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getSupportFragmentManager().getFragments().get(0).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        else finish();
    }
}