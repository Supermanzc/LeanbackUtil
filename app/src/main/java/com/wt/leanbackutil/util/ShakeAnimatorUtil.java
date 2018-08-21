package com.wt.leanbackutil.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import static android.view.View.FOCUS_RIGHT;

/**
 * Created by DELL on 2018/8/21.
 */

public class ShakeAnimatorUtil {

    //定义一个横向抖动的动画
    private ObjectAnimator translationAnimatorX;
    private ObjectAnimator translationAnimatorY;
    private static final int DURATION = 500;

    private static ShakeAnimatorUtil INSTANCE;
    private ShakeAnimatorUtil(){}

    public static ShakeAnimatorUtil getInstance(){
        synchronized (ShakeAnimatorUtil.class){
            if(INSTANCE == null){
                INSTANCE = new ShakeAnimatorUtil();
            }
            return INSTANCE;
        }
    }

    /**
     * 设置横向滚动效果
     *
     * @param newView
     */
    public void setHorizontalShakeAnimator(View newView) {
        //动画种类：X轴平移，后面的值为移动参数，正值为右，负值为左（Y轴正值为下，负值为上）
        translationAnimatorX = ObjectAnimator.ofFloat(newView, "translationX", 0f, 15f, 0f, -15f, 0f, 15f, 0f, -15f, 0f);
        //用于控制动画快慢节奏，此处使用系统自带的线性Interpolator（匀速），此外还有各种变速Interpolator
        translationAnimatorX.setInterpolator(new LinearInterpolator());
        //设置动画重复次数，ValueAnimator.INFINITE即-1表示用于一直重复
        translationAnimatorX.setRepeatCount(ValueAnimator.INFINITE);
        translationAnimatorX.setDuration(DURATION);
    }

    public void setVerticalShakeAnimator(View newView) {
        //动画种类：X轴平移，后面的值为移动参数，正值为右，负值为左（Y轴正值为下，负值为上）
        translationAnimatorY = ObjectAnimator.ofFloat(newView, "translationY", 0f, 15f, 0f, -15f, 0f, 15f, 0f, -15f, 0f);
        //用于控制动画快慢节奏，此处使用系统自带的线性Interpolator（匀速），此外还有各种变速Interpolator
        translationAnimatorY.setInterpolator(new LinearInterpolator());
        //设置动画重复次数，ValueAnimator.INFINITE即-1表示用于一直重复
        translationAnimatorY.setRepeatCount(ValueAnimator.INFINITE);
        translationAnimatorY.setDuration(DURATION);
    }

    /**
     * 开始横向抖动动画的方法，非外部调用
     */
    private void startHorizontalShakeAnimator() {
        //此处判断动画是否已经在run，若是则不重新调用start方法，避免重复触发导致抖动的不流畅
        if (translationAnimatorX != null && !translationAnimatorX.isRunning()) {
            //结束纵向动画，非本身横向动画
            endVerticalAnimator();
            translationAnimatorX.setRepeatCount(ValueAnimator.INFINITE);
            translationAnimatorX.start();
        }
    }

    private void endHorizontalAnimator() {
        if (translationAnimatorX != null) {
            //结束纵向动画，调用end()动画会到动画周期的最后一帧然后停止，调用cancel()动画会停止时间轴，停止在中间状态
            translationAnimatorX.end();
        }
    }

    private void endVerticalAnimator() {
        if (translationAnimatorY != null) {
            //结束纵向动画，调用end()动画会到动画周期的最后一帧然后停止，调用cancel()动画会停止时间轴，停止在中间状态
            translationAnimatorY.end();
        }
    }

    /**
     * 当按键抬起时，使动画只执行完当前周期，便自动结束
     */
    public void completeOneShakeCycle() {
        if (translationAnimatorX != null && translationAnimatorX.isRunning()) {
            //修改动画的重复次数为0，即只执行1次
            translationAnimatorX.setRepeatCount(0);
        }
        if (translationAnimatorY != null && translationAnimatorY.isRunning()) {
            translationAnimatorY.setRepeatCount(0);
        }
    }

    public void endShakeAnimator() {
        endHorizontalAnimator();  //调用end()即可，具体方法略
        endVerticalAnimator();
    }

    public void cancelAllShakeAnimator() {
        if (translationAnimatorX != null) {
            translationAnimatorX.cancel();
        }
        if (translationAnimatorY != null) {
            translationAnimatorY.cancel();
        }
    }

    /**
     * 需要传入焦点所在的view，以及当前的keyEvent
     *
     * @param itemView
     * @param event
     * @return
     */
    public boolean isNeedShake(ViewGroup parent, View itemView, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                //当按下左键时，调用focusSearch方法获得左方向上是否还有下一个可以获得焦点的view
                //若有，则不需要抖动，若无，需要抖动
                if (null == FocusFinder.getInstance().findNextFocus(parent, itemView, View.FOCUS_LEFT)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (null == FocusFinder.getInstance().findNextFocus(parent, itemView, View.FOCUS_RIGHT)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (null == FocusFinder.getInstance().findNextFocus(parent, itemView, View.FOCUS_UP)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (null == FocusFinder.getInstance().findNextFocus(parent, itemView, View.FOCUS_DOWN)) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }
}
