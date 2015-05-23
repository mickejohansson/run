package org.mikaeljohansson.run.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
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

    @InjectView(R.id.current_distance)
    TextView mCurrentDistanceTextView;

    @InjectView(R.id.layout_connection_overlay)
    View mConnectionOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WatchViewStub watchViewStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        watchViewStub.setOnLayoutInflatedListener(watchViewStub1 -> ButterKnife.inject(SpeedometerActivity.this));

        new SpeedometerPresenter(this);
    }

    @Override
    public void hideConnectionOverlay() {
        mConnectionOverlay.setVisibility(View.GONE);
    }

    @Override
    public void setCurrentSpeed(double speed) {
        String text = String.format("%.1f", speed);
        mCurrentSpeedTextView.setText(text);
    }

    @Override
    public void setAverageSpeed(double speed) {
        String text = String.format("%.1f", speed);
        mAverageSpeedTextView.setText(text);
    }

    @Override
    public void setCurrentDistance(float distance) {
        String text = String.format("%d", Math.round(distance));
        mCurrentDistanceTextView.setText(text);
    }
}
