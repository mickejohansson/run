package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

import org.mikaeljohansson.run.business.SpeedometerService;
import org.mikaeljohansson.run.business.WorkoutLog;
import org.mikaeljohansson.run.data.GpxParser;

import java.util.ArrayList;
import java.util.Date;

import rx.Observable;
import rx.Subscription;
import rx.android.view.OnClickEvent;

public class SpeedometerPresenter {

    private final Painter mPainter;
    private final SpeedometerService mSpeedometerService;
    private ArrayList<Subscription> mSubscriptions = new ArrayList<>();

    public SpeedometerPresenter(@NonNull Painter painter) {
        mPainter = painter;

        mSpeedometerService = SpeedometerServiceFactory.getSpeedometerService();

        mSpeedometerService.getConnectionObservable()
                .subscribe(isConnected -> mPainter.onServiceStarted());

        mPainter.getStartButtonObservable().subscribe(onClickEvent -> startWorkout());
        mPainter.getStopButtonObservable().subscribe(onClickEvent -> stopWorkout());
    }

    private void stopWorkout() {
        mPainter.hideStopButton();

        GpxParser.writeGpxFile(new Date().toString() + ".gpx", WorkoutLog.getLog());
        WorkoutLog.clear();
    }

    private void startWorkout() {
        resetSubscriptions();

        mPainter.showStopButton();

        Subscription subscription = mSpeedometerService.getLocationObservable()
                .subscribe(location -> WorkoutLog.add(location));
        mSubscriptions.add(subscription);

        subscription = mSpeedometerService.getCurrentSpeedObservable()
                .subscribe(speed -> mPainter.setCurrentSpeed(minsPerKm(speed)));
        mSubscriptions.add(subscription);

        subscription = mSpeedometerService.getAverageSpeedObservable(10)
                .subscribe(speed -> mPainter.setAverageSpeed(minsPerKm(speed)));
        mSubscriptions.add(subscription);

        subscription = mSpeedometerService.getDistanceObservable()
                .scan((float1, float2) -> float1 + float2)
                .subscribe(distance -> mPainter.setCurrentDistance(distance));
        mSubscriptions.add(subscription);

        mPainter.showSpeedometerScreen();
    }

    private void resetSubscriptions() {
        for (Subscription subscription : mSubscriptions) {
            subscription.unsubscribe();
        }
        mSubscriptions.clear();
        mPainter.resetSpeedometerValues();
    }

    private double minsPerKm(Float meterPerSecond) {
        return 1000 / (60 * meterPerSecond);
    }

    public interface Painter {
        Observable<OnClickEvent> getStartButtonObservable();

        Observable<OnClickEvent> getStopButtonObservable();

        void onServiceStarted();

        void setCurrentSpeed(double speed);

        void setAverageSpeed(double speed);

        void setCurrentDistance(float distance);

        void showSpeedometerScreen();

        void resetSpeedometerValues();

        void showStopButton();

        void hideStopButton();
    }
}
