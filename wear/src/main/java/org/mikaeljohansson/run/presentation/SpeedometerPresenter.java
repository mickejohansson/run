package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

import org.mikaeljohansson.run.data.LocationRepository;

public class SpeedometerPresenter implements LocationRepository.Listener {

    private final Painter mPainter;
    private final LocationRepository mLocationRepository;

    public SpeedometerPresenter(@NonNull Painter painter) {
        mPainter = painter;

        mLocationRepository = LocationRepository.getInstance();
        mLocationRepository.registerListener(this);
    }

    public interface Painter {

    }
}
