package org.mikaeljohansson.run.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ScrollView;

/**
 * Scrolls vertically, and snaps to multiples of the screen height.
 */
public class SnappingScrollView extends ScrollView {

    private int mScreenHeight;
    private boolean mBlockScrolling;

    public SnappingScrollView(Context context) {
        super(context);
        setup();
    }

    public SnappingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public SnappingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        mScreenHeight = getScreenHeight();

        setOnTouchListener((view, event) -> {
            if (mBlockScrolling) {
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_CANCEL) {
                int index = Math.round((getScrollY() + mScreenHeight / 2) / mScreenHeight);

                smoothScrollTo(getScrollX(), index * mScreenHeight);

                return true;
            }
            return false;
        });
    }

    public void goToScreen(int index) {
        smoothScrollTo(getScrollX(), index * mScreenHeight);
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
}
