package org.mikaeljohansson.run.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class FullscreenLayout extends RelativeLayout {
    public FullscreenLayout(Context context) {
        super(context);
    }

    public FullscreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullscreenLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int measureSpecX = MeasureSpec.makeMeasureSpec(size.x, MeasureSpec.EXACTLY);
        int measureSpecY = MeasureSpec.makeMeasureSpec(size.y, MeasureSpec.EXACTLY);
        super.onMeasure(measureSpecX, measureSpecY);
    }
}
