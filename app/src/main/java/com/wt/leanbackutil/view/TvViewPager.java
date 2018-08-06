package com.wt.leanbackutil.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by DELL on 2018/8/6.
 * @author 适用于TV端
 */

public class TvViewPager extends ViewPager {

    public TvViewPager(Context context) {
        super(context);
    }

    public TvViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //禁止左右滑动
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
