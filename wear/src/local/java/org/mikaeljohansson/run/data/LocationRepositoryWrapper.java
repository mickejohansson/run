package org.mikaeljohansson.run.data;

import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.mikaeljohansson.run.RunApplication;

import java.util.ArrayList;

public class LocationRepositoryWrapper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final GoogleApiClient mGoogleApiClient;
    private ArrayList<Location> mLocations;
    private int mCurrentLocationIndex = 0;

    public LocationRepositoryWrapper() {
        mGoogleApiClient = new GoogleApiClient.Builder(RunApplication.getAppContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    public LocationRepository getLocationRepository() {
        mLocations = GpxParser.parse("run.gpx");

        return new LocationRepository();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.setMockMode(mGoogleApiClient, true);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                Location mockLocation = mLocations.get(mCurrentLocationIndex);
                mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                mockLocation.setAccuracy(Criteria.ACCURACY_FINE);
                if (mCurrentLocationIndex > 0) {
                    Location previousLocation = mLocations.get(mCurrentLocationIndex - 1);
                    mockLocation.setSpeed(1000 * mockLocation.distanceTo(previousLocation) / (mockLocation.getTime() - previousLocation.getTime()));
                }
                System.out.println("Setting mock loc " + mockLocation);
                LocationServices.FusedLocationApi.setMockLocation(mGoogleApiClient, mockLocation);
                mCurrentLocationIndex = (mCurrentLocationIndex + 1) % (mLocations.size() - 1);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
