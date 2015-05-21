package org.mikaeljohansson.run.data;

import android.location.Criteria;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

public class LocationRepository {

    private final ArrayList<Location> mLocations;
    private PublishSubject<Location> mPublishSubject;
    private int mCurrentLocationIndex = 0;

    public LocationRepository() {
        mPublishSubject = PublishSubject.create();

        mLocations = GpxParser.parse("run.gpx");

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
                mPublishSubject.onNext(mockLocation);
                mCurrentLocationIndex = (mCurrentLocationIndex + 1) % (mLocations.size() - 1);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);


    }

    public Observable<Location> getLocationObservable() {
        return mPublishSubject.asObservable();
    }
}
