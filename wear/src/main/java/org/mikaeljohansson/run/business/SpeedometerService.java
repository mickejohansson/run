package org.mikaeljohansson.run.business;

import android.location.Location;

import org.mikaeljohansson.run.data.LocationRepository;
import org.mikaeljohansson.run.data.LocationRepositoryWrapper;

import rx.Observable;
import rx.functions.Func1;
import rx.observables.MathObservable;

public class SpeedometerService {
    private static SpeedometerService sInstance;
    private final Observable<Location> mLocationObservable;

    private SpeedometerService() {
        LocationRepository mLocationRepository = LocationRepositoryWrapper.getLocationRepository();
        mLocationObservable = mLocationRepository.getLocationObservable();
    }

    public static SpeedometerService getInstance() {
        if (sInstance == null) {
            sInstance = new SpeedometerService();
        }

        return sInstance;
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
        return getCurrentSpeedObservable().window(windowSize).flatMap(new Func1<Observable<Float>, Observable<Float>>() {
            @Override
            public Observable<Float> call(Observable<Float> window) {
                return MathObservable.averageFloat(window);
            }
        });
    }
}
