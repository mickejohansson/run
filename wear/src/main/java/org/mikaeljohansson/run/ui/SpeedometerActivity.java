package org.mikaeljohansson.run.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import org.mikaeljohansson.run.R;
import org.mikaeljohansson.run.presentation.SpeedometerPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SpeedometerActivity extends Activity implements SpeedometerPresenter.Painter {

    @InjectView(R.id.current_speed)
    TextView mCurrentSpeedTextView;

    @InjectView(R.id.average_speed)
    TextView mAverageSpeedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WatchViewStub watchViewStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        watchViewStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                ButterKnife.inject(SpeedometerActivity.this);
            }
        });


        new SpeedometerPresenter(this);
    }

    @Override
    public void setCurrentSpeed(float speed) {
        mCurrentSpeedTextView.setText("Current: " + speed);
    }

    @Override
    public void setAverageSpeed(float speed) {
        mAverageSpeedTextView.setText("Average: " + speed);
    }
}
