package org.mikaeljohansson.run.data;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;
import org.mikaeljohansson.run.BaseTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.functions.Action1;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class LocationRepositoryTest extends BaseTest {

    @Mock
    LocationManager mLocationManager;

    private LocationRepository mLocationRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mLocationRepository = new LocationRepository();
    }

    @Test
    public void onGetLocationObservable_startListeningForLocation() {
        ArgumentCaptor<LocationListener> locationListener = ArgumentCaptor.forClass(LocationListener.class);

        Observable<Location> locationObservable = mLocationRepository.getLocationObservable();
        locationObservable.subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
            }
        });

        verify(mLocationManager).requestLocationUpdates(anyString(), anyLong(), anyFloat(), locationListener.capture());
    }
}