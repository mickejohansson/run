package org.mikaeljohansson.run.business;

import android.location.Location;

import org.mikaeljohansson.run.data.LocationRepository;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
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
        return mLocationObservable.map(new Func1<Location, Float>() {
            @Override
            public Float call(Location location) {
                return location.getSpeed();
            }
        });
    }

    public Observable<Float> getAverageSpeedObservable(int windowSize) {
        return getCurrentSpeedObservable().filter(new Func1<Float, Boolean>() {
            @Override
            public Boolean call(Float speed) {
                // Ignore speeds lower than 1 m/s.
                return speed > 1.0f;
            }
        }).window(windowSize, 1).flatMap(new Func1<Observable<Float>, Observable<Float>>() {
            @Override
            public Observable<Float> call(Observable<Float> window) {
                return MathObservable.averageFloat(window);
            }
        });
    }

    private Observable<Location> getFilteredLocatoinObservable() {
        return mLocationObservable.doOnNext(new Action1<Location>() {

            @Override
            public void call(Location location) {
                if (mLastLocation == null) {
                    mLastLocation = location;
                }
            }
        }).skipWhile(new Func1<Location, Boolean>() {
            @Override
            public Boolean call(Location location) {
                return location.distanceTo(mLastLocation) < MIN_DISTANCE;
            }
        }).doOnNext(new Action1<Location>() {
            @Override
            public void call(Location location) {
                mLastLocation = null;
            }
        });
    }

    public Observable<Float> getDistanceObservable() {
        return getFilteredLocatoinObservable().buffer(2, 1).map(new Func1<List<Location>, Float>() {
            @Override
            public Float call(List<Location> locations) {
                return locations.get(0).distanceTo(locations.get(1));
            }
        });
    }
}
