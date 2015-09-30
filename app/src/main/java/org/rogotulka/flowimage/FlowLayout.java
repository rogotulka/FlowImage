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
    private int newY;
    private StateView mStateView;
    private int newTopY;
    private int newBottomY;

    private class StateView{
        private int left;
        private int top;
        private int right;
        private int bottom;


    }

    private  Runnable mRunnable = new Runnable() {
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
        Log.d("Flow Layout", "onLayout" + String.valueOf(downY));
//        super.onLayout(changed, left, top, right, bottom);
        if (dragView != null) {
            dragView.layout(dragView.getLeft(), newTopY, dragView.getRight(), newBottomY);
        }

//        invalidate();
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
//
                            mStateView = new StateView();
                            mStateView.bottom = dragView.getBottom();
                            mStateView.top = dragView.getTop();
                        }

                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("Flow Layout", "ACTION_MOVE" + String.valueOf(event.getY()));
                if (isDrag && dragView != null) {
                    newY = (int) event.getY();
                    newTopY = mStateView.top - (downY-newY);
                    newBottomY = mStateView.bottom - (downY-newY);
                    if(dragView!=null){
                        postRequestLayout();
                    }

                }

                break;

            case MotionEvent.ACTION_CANCEL:
                isDrag = false;
                break;
            case MotionEvent.ACTION_UP:
                isDrag = false;
                newTopY = mStateView.top;
                newBottomY = mStateView.bottom;
                if(dragView!=null){
                    postRequestLayout();
                }
                dragView = null;

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
        Log.d("vkjdfvfv", "recGetView");
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

    private void postRequestLayout(){
        getHandler().post(mRunnable);
    }
}
