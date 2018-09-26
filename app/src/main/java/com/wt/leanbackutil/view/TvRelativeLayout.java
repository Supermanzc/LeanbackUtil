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
 * Created by DELL on 2018/8/21.
 */

public class TvRelativeLayout extends RelativeLayout {

    public TvRelativeLayout(Context context) {
        super(context);
    }

    public TvRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        return super.dispatchGenericFocusedEvent(event);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        LogUtil.e("focusSearch----------" + focused + "      dr=" + direction);
        return super.focusSearch(focused, direction);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction) {
        super.addFocusables(views, direction);
    }

    @Override
    public View focusSearch(int direction) {
        LogUtil.e("focusSearch----------" + direction);
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
}
