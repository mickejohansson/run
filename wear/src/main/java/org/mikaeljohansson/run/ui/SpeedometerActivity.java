package org.mikaeljohansson.run.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.mikaeljohansson.run.R;
import org.mikaeljohansson.run.presentation.SpeedometerPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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

        ButterKnife.inject(SpeedometerActivity.this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        new SpeedometerPresenter(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        String text = "0 m";

        if (distance >= 1000) {
            text = String.format("%.1f km", distance / 1000);
        } else {
            text = String.format("%d m", Math.round(distance));
        }
        mCurrentDistanceTextView.setText(text);
    }
}
