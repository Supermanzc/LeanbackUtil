package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ShakeAnimatorUtil;

import java.util.ArrayList;

/**
 * @author junyan
 *         tv
 */

public class TvRelativeLayout extends RelativeLayout {

    private View defaultView;

    public TvRelativeLayout(Context context) {
        super(context);
        init();
    }

    public TvRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(true);
        setChildrenDrawingOrderEnabled(true);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            View view = getFocusedChild();
            if (ShakeAnimatorUtil.getInstance().isNeedShake(this, view, event)) {
                ShakeAnimatorUtil.getInstance().setHorizontalShakeAnimator(view);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        LogUtil.e("requestFocus-----------------direction=" + previouslyFocusedRect);
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        LogUtil.e("onRequestFocusInDescendants-----------------direction=" + previouslyFocusedRect);
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        return super.dispatchGenericFocusedEvent(event);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        return super.focusSearch(focused, direction);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction) {
        super.addFocusables(views, direction);
    }

    @Override
    public View focusSearch(int direction) {
        return super.focusSearch(direction);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
    }

    @Override
    public View findFocus() {
        return super.findFocus();
    }

    @Override
    public void focusableViewAvailable(View v) {
        super.focusableViewAvailable(v);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        LogUtil.e("addFocusables-----------direction=" + direction + "   focusableMode=" + focusableMode);
        super.addFocusables(views, direction, focusableMode);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        LogUtil.e("getChildDrawingOrder------------childCount=" + childCount + "   i=" + i);
        return super.getChildDrawingOrder(childCount, i);
    }
}
