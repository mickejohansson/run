package org.mikaeljohansson.run.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ScrollView;

/**
 * Scrolls vertically, and snaps to multiples of the screen height.
 */
public class SnappingScrollView extends ScrollView {

    private int mScreenHeight;
    private boolean mBlockScrolling;
    private GestureDetector mGestureDetector;
    private Context mContext;
    private int mCurrentScreen;

    public SnappingScrollView(Context context) {
        super(context);
        setup(context);
    }

    public SnappingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public SnappingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        mScreenHeight = getScreenHeight();
        mContext = context;

        mGestureDetector = new GestureDetector(context, new FlingGestureListener());

        setOnTouchListener((view, event) -> {
            if (mBlockScrolling) {
                return true;
            }

            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_CANCEL) {
                int index = Math.round((getScrollY() + mScreenHeight / 2) / mScreenHeight);

                goToScreen(index);

                return true;
            }
            return false;
        });
    }

    public void goToScreen(int index) {
        smoothScrollTo(getScrollX(), index * mScreenHeight);
        mCurrentScreen = index;
    }

    private void goToNextScreen() {
        goToScreen(mCurrentScreen + 1);
    }

    private void goToPreviousScreen() {
        goToScreen(mCurrentScreen - 1);
    }

    private int getScreenHeight() {
        if (isInEditMode()) {
            return 280;
        } else {
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
    }

    public void setScrollBlock(boolean block) {
        mBlockScrolling = block;
    }

    private class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (velocityY > ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity()) {
                goToPreviousScreen();
                return true;
            } else if (velocityY < -ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity()) {
                goToNextScreen();
                return true;
            }

            return false;
        }
    }
}
