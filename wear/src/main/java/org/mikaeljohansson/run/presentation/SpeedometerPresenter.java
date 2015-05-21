package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

import org.mikaeljohansson.run.business.SpeedometerService;

import rx.functions.Action1;
import rx.functions.Func2;

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

        mSpeedometerService.getDistanceObservable().scan(new Func2<Float, Float, Float>() {
            @Override
            public Float call(Float float1, Float float2) {
                return float1 + float2;
            }
        }).subscribe(new Action1<Float>() {
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
