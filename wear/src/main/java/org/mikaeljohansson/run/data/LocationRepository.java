package org.mikaeljohansson.run.data;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.mikaeljohansson.run.RunApplication;

import rx.Observable;
import rx.subjects.PublishSubject;

public class LocationRepository implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static LocationRepository mInstance;
    private final GoogleApiClient mGoogleApiClient;
    private final PublishSubject<Location> mPublishSubject;

    private LocationRepository() {
        mGoogleApiClient = new GoogleApiClient.Builder(RunApplication.getAppContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        mPublishSubject = PublishSubject.create();
    }

    public static LocationRepository getInstance() {
        if (mInstance == null) {
            mInstance = new LocationRepository();
        }

        return mInstance;
    }

    public Observable<Location> getLocationObservable() {
        return mPublishSubject.asObservable();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mPublishSubject.onNext(location);
                System.out.println("bbb hello");
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mPublishSubject.onError(new Exception("Connection failed: " + connectionResult.toString()));
    }
}
