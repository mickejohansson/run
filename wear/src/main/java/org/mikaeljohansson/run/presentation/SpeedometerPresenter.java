package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

import org.mikaeljohansson.run.business.SpeedometerService;

public class SpeedometerPresenter {

    private final Painter mPainter;
    private final SpeedometerService mSpeedometerService;

    public SpeedometerPresenter(@NonNull Painter painter) {
        mPainter = painter;

        mSpeedometerService = SpeedometerServiceFactory.getSpeedometerService();

        mSpeedometerService.getConnectionObservable()
                .subscribe(isConnected -> onServiceStarted());

        mSpeedometerService.getCurrentSpeedObservable()
                .subscribe(speed -> mPainter.setCurrentSpeed(minsPerKm(speed)));

        mSpeedometerService.getAverageSpeedObservable(10)
                .subscribe(speed -> mPainter.setAverageSpeed(minsPerKm(speed)));

        mSpeedometerService.getDistanceObservable()
                .scan((float1, float2) -> float1 + float2)
                .subscribe(distance -> mPainter.setCurrentDistance(distance));
    }

    private void onServiceStarted() {
        mPainter.hideConnectionOverlay();
    }

    private double minsPerKm(Float meterPerSecond) {
        return 1000 / (60 * meterPerSecond);
    }

    public interface Painter {
        void hideConnectionOverlay();

        void setCurrentSpeed(double speed);

        void setAverageSpeed(double speed);

        void setCurrentDistance(float distance);
    }
}
