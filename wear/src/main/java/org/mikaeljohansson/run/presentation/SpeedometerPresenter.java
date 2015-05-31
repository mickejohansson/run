package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

import org.mikaeljohansson.run.business.SpeedometerService;

import java.util.ArrayList;

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

        mPainter.getStartButtonObservable().subscribe(onClickEvent -> subscribeToSpeedomater());
    }

    private void subscribeToSpeedomater() {
        resetSubscriptions();

        Subscription subscription = mSpeedometerService.getCurrentSpeedObservable()
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

        mPainter.setCurrentDistance(0.0f);
    }

    private double minsPerKm(Float meterPerSecond) {
        return 1000 / (60 * meterPerSecond);
    }

    public interface Painter {
        Observable<OnClickEvent> getStartButtonObservable();

        void onServiceStarted();

        void setCurrentSpeed(double speed);

        void setAverageSpeed(double speed);

        void setCurrentDistance(float distance);

        void showSpeedometerScreen();
    }
}
