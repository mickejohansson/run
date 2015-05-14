package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

import org.mikaeljohansson.run.business.SpeedometerService;

import rx.functions.Action1;

public class SpeedometerPresenter {

    private final Painter mPainter;
    private final SpeedometerService mSpeedometerService;

    public SpeedometerPresenter(@NonNull Painter painter) {
        mPainter = painter;

        mSpeedometerService = SpeedometerService.getInstance();
        mSpeedometerService.getCurrentSpeedObservable().subscribe(new Action1<Float>() {
            @Override
            public void call(Float speed) {
                mPainter.setCurrentSpeed(1000 / (60 * speed));
            }
        });

        mSpeedometerService.getAverageSpeedObservable(10).subscribe(new Action1<Float>() {
            @Override
            public void call(Float speed) {
                mPainter.setAverageSpeed(1000 / (60 * speed));
            }
        });
    }

    public interface Painter {
        void setCurrentSpeed(double speed);

        void setAverageSpeed(double speed);
    }
}
