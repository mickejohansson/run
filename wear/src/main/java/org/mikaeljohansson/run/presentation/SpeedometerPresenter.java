package org.mikaeljohansson.run.presentation;

import android.support.annotation.NonNull;

public class SpeedometerPresenter {

    private final Painter mPainter;

    public SpeedometerPresenter(@NonNull Painter painter) {
        mPainter = painter;
    }

    public interface Painter {

    }
}
