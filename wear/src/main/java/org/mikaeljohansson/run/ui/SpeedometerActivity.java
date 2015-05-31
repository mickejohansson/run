package org.mikaeljohansson.run.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.CircularButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.widget.TextView;

import org.mikaeljohansson.run.R;
import org.mikaeljohansson.run.presentation.SpeedometerPresenter;
import org.mikaeljohansson.run.widget.SnappingScrollView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SpeedometerActivity extends Activity implements SpeedometerPresenter.Painter {

    @InjectView(R.id.scroll_view)
    SnappingScrollView mScrollView;

    @InjectView(R.id.current_speed)
    TextView mCurrentSpeedTextView;

    @InjectView(R.id.average_speed)
    TextView mAverageSpeedTextView;

    @InjectView(R.id.current_distance)
    TextView mCurrentDistanceTextView;

    @InjectView(R.id.loading_icon)
    View mLoadingIcon;

    @InjectView(R.id.start_workout_button)
    CircularButton mStartWorkoutButton;

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

        mScrollView.setScrollBlock(true);
        mStartWorkoutButton.setOnClickListener(view -> {
            mScrollView.goToScreen(1);
        });

        startLoadingAnimation();

        new SpeedometerPresenter(this);
    }

    private void startLoadingAnimation() {
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.loading);
        mLoadingIcon.startAnimation(scale);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onServiceStarted() {
        mLoadingIcon.clearAnimation();
        showStartWorkoutButton();
        mScrollView.setScrollBlock(false);
    }

    private void showStartWorkoutButton() {
        mLoadingIcon.animate().scaleYBy(5).scaleXBy(5).setInterpolator(new AnticipateInterpolator()).setDuration(400).start();

        Animation showWorkoutButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.show_workout_button);
        showWorkoutButtonAnimation.setStartOffset(300);
        mStartWorkoutButton.setVisibility(View.VISIBLE);
        mStartWorkoutButton.startAnimation(showWorkoutButtonAnimation);
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
