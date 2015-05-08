package org.mikaeljohansson.run.data;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.functions.Action1;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class LocationRepositoryTest {

    @Mock
    LocationManager mLocationManager;

    private LocationRepository mLocationRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mLocationRepository = new LocationRepository(mLocationManager);
    }

    @Test
    public void onLocationUpdate_notifySubscribers() {
        ArgumentCaptor<LocationListener> locationListener = ArgumentCaptor.forClass(LocationListener.class);

        mLocationRepository.getLocationObservable().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
            }
        });

        verify(mLocationManager).requestLocationUpdates(anyString(), anyLong(), anyFloat(), locationListener.capture());
    }
}