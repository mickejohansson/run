package org.mikaeljohansson.run.business;

import android.location.Location;

import org.mikaeljohansson.run.data.LocationRepository;

import rx.Observable;
import rx.observables.MathObservable;

public class SpeedometerService {
    private static final float MIN_DISTANCE = 20;
    private final Observable<Location> mLocationObservable;
    Location mLastLocation = null;

    public SpeedometerService(LocationRepository locationRepository) {
        mLocationObservable = locationRepository.getLocationObservable();
    }

    public SpeedometerService() {
        this(new LocationRepository());
    }

    public Observable<Float> getCurrentSpeedObservable() {
        return mLocationObservable.map(location -> location.getSpeed());
    }

    public Observable<Float> getAverageSpeedObservable(int windowSize) {
        return getCurrentSpeedObservable()
                .filter(speed -> speed > 1.0f)
                .window(windowSize, 1)
                .flatMap(window -> MathObservable.averageFloat(window));
    }

    private Observable<Location> getFilteredLocationObservable() {
        return mLocationObservable
                .doOnNext(location -> {
                    if (mLastLocation == null) {
                        mLastLocation = location;
                    }
                })
                .skipWhile(location -> location.distanceTo(mLastLocation) < MIN_DISTANCE)
                .doOnNext(location -> mLastLocation = null);
    }

    public Observable<Float> getDistanceObservable() {
        return getFilteredLocationObservable()
                .buffer(2, 1)
                .map(locations -> locations.get(0).distanceTo(locations.get(1)));
    }
}
