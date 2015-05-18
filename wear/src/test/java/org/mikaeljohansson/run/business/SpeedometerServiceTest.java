package org.mikaeljohansson.run.business;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.mikaeljohansson.run.BaseTest;
import org.mikaeljohansson.run.data.LocationRepository;
import org.mikaeljohansson.run.data.LocationRepositoryWrapper;
import org.mockito.Mock;

import rx.subjects.PublishSubject;

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

        mSpeedometerService = SpeedometerService.getInstance();
    }

    @Test
    public void onReceivingLocationUpdates_PassOnCurrentSpeed() {
        PublishSubject<Float> sub = PublishSubject.create();
        mSpeedometerService.getCurrentSpeedObservable().subscribe(sub);

        float speed = 5.4f;
        Location location = new Location("MyFakeProvider");
        location.setSpeed(speed);
        mLocationSubject.onNext(location);
        verify(sub).onNext(speed);

    }
}
