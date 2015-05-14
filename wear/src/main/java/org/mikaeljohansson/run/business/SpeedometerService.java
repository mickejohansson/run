package org.mikaeljohansson.run.business;

import android.location.Location;

import org.mikaeljohansson.run.data.LocationRepository;
import org.mikaeljohansson.run.data.LocationRepositoryWrapper;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.observables.MathObservable;

public class SpeedometerService {
    private static SpeedometerService sInstance;
    private final Observable<Location> mLocationObservable;

    private SpeedometerService() {
        LocationRepositoryWrapper locationRepositoryWrapper = new LocationRepositoryWrapper();
        LocationRepository locationRepository = locationRepositoryWrapper.getLocationRepository();
        mLocationObservable = locationRepository.getLocationObservable();
    }

    public static SpeedometerService getInstance() {
        if (sInstance == null) {
            sInstance = new SpeedometerService();
        }

        return sInstance;
    }

    public Observable<Float> getCurrentSpeedObservable() {
        return mLocationObservable.buffer(4, 1).map(new Func1<List<Location>, Float>() {
            @Override
            public Float call(List<Location> locations) {
                System.out.println(locations.get(0).getLatitude() + ":" + locations.get(0).getLongitude());
                System.out.println(locations.get(3).getLatitude() + ":" + locations.get(3).getLongitude());
                System.out.println("dist:" + locations.get(0).distanceTo(locations.get(3)));
                System.out.println("time:" + (locations.get(3).getTime() - locations.get(0).getTime()));
                return locations.get(0).distanceTo(locations.get(3)) * 1000 / (locations.get(3).getTime() - locations.get(0).getTime());
            }
        });
    }

    public Observable<Float> getAverageSpeedObservable(int windowSize) {
        return getCurrentSpeedObservable().window(windowSize, 1).flatMap(new Func1<Observable<Float>, Observable<Float>>() {
            @Override
            public Observable<Float> call(Observable<Float> window) {
                return MathObservable.averageFloat(window);
            }
        });
    }
}
