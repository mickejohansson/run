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
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenHeight = size.y;

        setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_CANCEL) {
                int index = Math.round((getScrollY() + mScreenHeight / 2) / mScreenHeight);

                smoothScrollTo(getScrollX(), index * mScreenHeight);

                return true;
            }
            return false;
        });
    }
}
