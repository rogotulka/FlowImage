package org.rogotulka.flowimage;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by a.rozhkova on 31.07.2015.
 */
public class FlowLayout extends FrameLayout {

    private boolean isDrag;
    private View dragView;
    private int downY;
    private int oldY;
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            requestLayout();
        }
    };


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d("Flow Layout", "onLayout" +String.valueOf(downY));
        if (dragView != null) {
            dragView.layout(dragView.getLeft(), dragView.getTop() - downY, dragView.getRight(), dragView.getBottom());
        }
        super.onLayout(changed, left, top, right, bottom);
        invalidate();
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
            case MotionEvent.ACTION_DOWN:
                isDrag = true;
                downY = (int) event.getY();
                Log.d("Flow Layout", "ACTION_DOWN" + String.valueOf(event.getY()));
                if (dragView == null) {
                    for (View view : recursiveGetView(getRootView())) {
                        int[] l = new int[2];
                        view.getLocationOnScreen(l);
                        if (isTouchableArea(l[0], l[1], view.getWidth(), view.getHeight(), (int) event.getX(), (int) event.getY())) {
                            dragView = view;

                            oldY = (int) view.getY();
                        }

                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("Flow Layout", "ACTION_MOVE" + String.valueOf(event.getY()));
                if (isDrag && dragView != null) {
                    downY = (int) event.getY();
                    if(dragView!=null){
                        requestLayout();
                    }

                }

                break;

            case MotionEvent.ACTION_CANCEL:
                isDrag = false;
                break;
            case MotionEvent.ACTION_UP:
                isDrag = false;
                downY = oldY;
                if(dragView!=null){
                    requestLayout();
                }

                break;
        }

        return true;
    }

    private boolean isTouchableArea(int topX, int topY, int width, int height, int eventX, int eventY) {
        if (topX <= eventX && topY <= eventY && topX + width >= eventX && topY + height >= eventY) {
            return true;
        }
        return false;
    }

    private ArrayList<View> recursiveGetView(View root) {
        ArrayList<View> views = new ArrayList<>();
        if (root instanceof ViewGroup) {
            ViewGroup viewGroup = ((ViewGroup) root);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                views.addAll(recursiveGetView(viewGroup.getChildAt(i)));
            }
        }else{
            views.add(root);
        }
        return views;
    }
}
