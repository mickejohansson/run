package org.mikaeljohansson.run.business;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.mikaeljohansson.run.BaseTest;
import org.mikaeljohansson.run.data.LocationRepository;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.observers.TestObserver;
import rx.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpeedometerServiceTest extends BaseTest {

    SpeedometerService mSpeedometerService;

    @Mock
    LocationRepository mLocationRepository;

    PublishSubject<Location> mLocationSubject;

    @Before
    public void setThingsUp() {
        mLocationSubject = PublishSubject.create();
        when(mLocationRepository.getLocationObservable()).thenReturn(mLocationSubject.asObservable());

        mSpeedometerService = new SpeedometerService(mLocationRepository);
    }

    @Test
    public void onReceivingLocationUpdates_PassOnCurrentSpeed() {
        Observer<Float> delegate = mock(Observer.class);
        TestObserver<Float> testObserver = new TestObserver(delegate);
        mSpeedometerService.getCurrentSpeedObservable().subscribe(testObserver);

        float speed = 5.4f;
        Location mockLocation = mock(Location.class);
        when(mockLocation.getSpeed()).thenReturn(speed);
        mLocationSubject.onNext(mockLocation);
        verify(delegate).onNext(speed);
    }

    @Test
    public void onReceiveMultipleLocations_PassOnMultipleSpeeds() {
        Observer<Float> delegate = mock(Observer.class);
        TestObserver<Float> testObserver = new TestObserver(delegate);
        mSpeedometerService.getCurrentSpeedObservable().subscribe(testObserver);

        List<Float> speeds = new ArrayList<Float>();
        speeds.add(2.0f);
        speeds.add(2.1f);
        speeds.add(2.2f);
        speeds.add(2.3f);
        speeds.add(2.4f);

        Location mockLocation1 = mock(Location.class);
        when(mockLocation1.getSpeed()).thenReturn(speeds.get(0));

        Location mockLocation2 = mock(Location.class);
        when(mockLocation2.getSpeed()).thenReturn(speeds.get(1));

        Location mockLocation3 = mock(Location.class);
        when(mockLocation3.getSpeed()).thenReturn(speeds.get(2));

        Location mockLocation4 = mock(Location.class);
        when(mockLocation4.getSpeed()).thenReturn(speeds.get(3));

        Location mockLocation5 = mock(Location.class);
        when(mockLocation5.getSpeed()).thenReturn(speeds.get(4));

        mLocationSubject.onNext(mockLocation1);
        mLocationSubject.onNext(mockLocation2);
        mLocationSubject.onNext(mockLocation3);
        mLocationSubject.onNext(mockLocation4);
        mLocationSubject.onNext(mockLocation5);

        testObserver.assertReceivedOnNext(speeds);
    }

    @Test
    public void onReceiveMultipleLocations_passOnDistance() {
        Observer<Float> delegate = mock(Observer.class);
        TestObserver<Float> testObserver = new TestObserver(delegate);
        mSpeedometerService.getDistanceObservable().subscribe(testObserver);

        Location mockLocation;
        for (int i = 0; i < 10; i++) {
            mockLocation = mock(Location.class);
            when(mockLocation.distanceTo(any(Location.class))).thenReturn(50.0f);
            mLocationSubject.onNext(mockLocation);
        }

        assert (testObserver.getOnNextEvents().size() == 9);
    }

    @Test
    public void onReceiveLocations_whenDistanceIsFarEnough_passOnDistances() {
        Observer<Float> delegate = mock(Observer.class);
        TestObserver<Float> testObserver = new TestObserver(delegate);
        mSpeedometerService.getDistanceObservable().subscribe(testObserver);

        Location mockLocation;
        for (int i = 0; i < 2; i++) {
            mockLocation = mock(Location.class);
            when(mockLocation.distanceTo(any(Location.class))).thenReturn(20.0f);
            mLocationSubject.onNext(mockLocation);
        }

        // We should just receive one event (only after having moved at least 20 meters)
        assert (testObserver.getOnNextEvents().size() == 1);
    }

    @Test
    public void onReceiveTooCloseLocations_dontPassOnDistances() {
        Observer<Float> delegate = mock(Observer.class);
        TestObserver<Float> testObserver = new TestObserver(delegate);
        mSpeedometerService.getDistanceObservable().subscribe(testObserver);

        Location mockLocation;
        for (int i = 0; i < 4; i++) {
            mockLocation = mock(Location.class);
            when(mockLocation.distanceTo(any(Location.class))).thenReturn(1.0f);
            mLocationSubject.onNext(mockLocation);
        }

        // We should just receive one event (only after having moved at least 20 meters)
        assert (testObserver.getOnNextEvents().size() == 0);
    }
}
