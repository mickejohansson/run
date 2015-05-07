package org.mikaeljohansson.run.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.mikaeljohansson.run.R;
import org.mikaeljohansson.run.presentation.SpeedometerPresenter;

public class SpeedometerActivity extends Activity implements SpeedometerPresenter.Painter {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SpeedometerPresenter(this);
    }
}
