package org.mikaeljohansson.run.data;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

public class LocationRepository {

    private final LocationManager mLocationManager;

    public LocationRepository(@NonNull LocationManager locationManager) {
        mLocationManager = locationManager;
    }

    public Observable<Location> getLocationObservable() {
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(final Subscriber<? super Location> subscriber) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        subscriber.onNext(location);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                    }
                });
            }
        });
    }
}
