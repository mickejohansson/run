package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

import org.mikaeljohansson.run.business.SpeedometerService;

import rx.functions.Action1;
import rx.observables.MathObservable;

public class SpeedometerPresenter {

    private final Painter mPainter;
    private final SpeedometerService mSpeedometerService;

    public SpeedometerPresenter(@NonNull Painter painter) {
        mPainter = painter;

        mSpeedometerService = SpeedometerServiceFactory.getSpeedometerService();
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

        MathObservable.sumFloat(mSpeedometerService.getDistanceObservable()).subscribe(new Action1<Float>() {
            @Override
            public void call(Float distance) {
                mPainter.setCurrentDistance(distance);
            }
        });
    }

    public interface Painter {
        void setCurrentSpeed(double speed);

        void setAverageSpeed(double speed);

        void setCurrentDistance(float distance);
    }
}
