package org.rogotulka.flowimage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by a.rozhkova on 31.07.2015.
 */
public class FlowLayout extends FrameLayout {

    private boolean isDrag;
    private View dragView;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isDrag) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        float x = event.getX();

        switch (action) {

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < getChildCount(); i++) {
                    final View child = getChildAt(i);
                    //child.touch
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    public void startDrag(View child) {

    }
}
