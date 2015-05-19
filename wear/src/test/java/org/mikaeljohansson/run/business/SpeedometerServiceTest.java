package org.mikaeljohansson.run.business;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.mikaeljohansson.run.BaseTest;
import org.mikaeljohansson.run.data.LocationRepository;
import org.mikaeljohansson.run.data.LocationRepositoryWrapper;
import org.mockito.Mock;

import rx.Observer;
import rx.observers.TestObserver;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpeedometerServiceTest extends BaseTest {

    SpeedometerService mSpeedometerService;

    @Mock
    LocationRepositoryWrapper mLocationRepositoryWrapper;

    @Mock
    LocationRepository mLocationRepository;

    PublishSubject<Location> mLocationSubject;

    @Before
    public void setThingsUp() {
        mLocationSubject = PublishSubject.create();
        when(mLocationRepositoryWrapper.getLocationRepository()).thenReturn(mLocationRepository);
        when(mLocationRepository.getLocationObservable()).thenReturn(mLocationSubject.asObservable());

        mSpeedometerService = new SpeedometerService(mLocationRepositoryWrapper);
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
}
