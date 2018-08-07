package com.open.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hailongqiu on 2018/4/17.
 */

public class PageRecyclerView extends RecyclerView {

    public PageRecyclerView(Context context) {
        super(context);
    }

    public PageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

     /* 添加翻页效果 start start start */

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    boolean mIsPageEnabled = false;

    public void setPagingEnabled(boolean isPage) {
        mIsPageEnabled = isPage;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
    }

    View mSaveView = null;
    int mSaveDirection = View.FOCUS_RIGHT;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        View focusView = this.getFocusedChild();
        if (focusView != null) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    }
                    View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                    if (rightView != null) {
                        final int parentRight = getWidth() - getPaddingRight();
                        final int childRight = rightView.getLeft() + rightView.getWidth();
                        Log.d("hailongqiu", " childRight:" + childRight + " parentRight:" + parentRight);
                        if (childRight > parentRight) {
                            setScrollPage(false);
                        } else {
                            rightView.requestFocusFromTouch();
                        }
                    } else {
                        setScrollPage(false);
                    }
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    }
                    View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                    if (leftView != null) {
                        final int parentLeft = getPaddingLeft();
                        final int childLeft = leftView.getLeft() - leftView.getScrollX();
                        Log.d("hailongqiu", "getScrollX:" + leftView.getScrollX() + " childLeft:" + childLeft + " parentRight:" + parentLeft);
                        if (childLeft < parentLeft) {
                            setScrollPage(true);
                        } else {
                            leftView.requestFocusFromTouch();
                        }
                    } else {
                        setScrollPage(true);
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onScrollStateChanged(int scrollState) {
        super.onScrollStateChanged(scrollState);
        Log.d("hailongqiu", "onScrollStateChanged scrollState:" + scrollState);
        if (scrollState == SCROLL_STATE_IDLE) {
            // 翻页后需要设置item焦点.
            if (null != mSaveView) {
                try {
                    View focusView = this.getFocusedChild();
                    View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, mSaveDirection);
                    if (null != rightView) {
                        rightView.requestFocusFromTouch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mSaveView = null;
            }
        }
    }

//    @Override
//    public void onScrolled(int dx, int dy) {
//        super.onScrolled(dx, dy);
////        if (null != mSaveView) {
//            View focusView = this.getFocusedChild();
//            View moveFocusView = FocusFinder.getInstance().findNextFocus(this, focusView, dx > 0 ? View.FOCUS_RIGHT : View.FOCUS_LEFT);
//            if (null != moveFocusView) {
//                moveFocusView.requestFocusFromTouch();
//            }
////            mSaveView = null;
////        }
//    }

    public void setScrollPage(boolean isLeft) {
        View focusView = this.getFocusedChild();
        mSaveView = focusView;
        mSaveDirection = isLeft ? View.FOCUS_LEFT : View.FOCUS_RIGHT;
        smoothScrollBy(isLeft ? -getWidth() : getWidth(), 0);
    }

//    public void setScrollPage(int width, boolean isLeft) {
//        View focusView = this.getFocusedChild();
//        mSaveView = focusView;
//        mSaveDirection = isLeft ? View.FOCUS_LEFT : View.FOCUS_RIGHT;
//        smoothScrollBy(isLeft ? -width : width, 0);
//    }

    /**
     * 获取可见焦点位置是否在第一个
     * (主要用于箭头显示判断)
     */
    public boolean isFirstItemVisible() {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) lm).findFirstCompletelyVisibleItemPosition();
            return position == 0;
        }
        return false;
    }

    /**
     * 获取可见焦点位置是否在最后一个
     * (主要用于箭头显示判断)
     */
    public boolean isLastItemVisible() {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) lm).findLastCompletelyVisibleItemPosition();
            return position == lm.getItemCount() - 1;
        }
        return false;
    }

    /* 添加翻页效果 end end end */

}
