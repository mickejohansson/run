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
                mPainter.setCurrentSpeed(speed);
            }
        });

        mSpeedometerService.getAverageSpeedObservable(100).subscribe(new Action1<Float>() {
            @Override
            public void call(Float speed) {
                mPainter.setAverageSpeed(speed);
            }
        });
    }

    public interface Painter {
        void setCurrentSpeed(float speed);

        void setAverageSpeed(float speed);
    }
}
