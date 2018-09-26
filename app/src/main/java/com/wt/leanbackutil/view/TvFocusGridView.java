package com.wt.leanbackutil.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import com.open.leanback.widget.VerticalGridView;
import com.owen.tvrecyclerview.utils.Loger;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

/**
 * Created by DELL on 2018/8/13.
 *
 * @author junyan
 *         <ol>
 *         <li>
 *         1、numColums必须大于1才可以使用该类
 *         </li>
 *         <li>
 *         主要是处理焦点向右移动和向下移动无法响应问题
 *         </li>
 *         </ol>
 */

public class TvFocusGridView extends VerticalGridView {

    private int mNumColumns = -1;

    private OnInBorderKeyEventListener mOnInBorderKeyEventListener;

    public TvFocusGridView(Context context) {
        super(context);
    }

    public TvFocusGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvFocusGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        final View nextFocusedView = findNextFocus(direction);
        if(hasInBorder(direction, nextFocusedView)) {
            return super.focusSearch(focused, direction);
        } else {
            return nextFocusedView;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result = super.dispatchKeyEvent(event);
        if (!result) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    result = null != mOnInBorderKeyEventListener && handleKeyDown(event.getKeyCode(), event);
                    break;
                case KeyEvent.ACTION_UP:
                    result = onKeyUp(event.getKeyCode(), event);
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    /**
     * 处理onKeyDown等事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    private boolean handleKeyDown(int keyCode, KeyEvent event) {
        int direction = keyCode2Direction(keyCode);

        if (direction == -1 || null == mOnInBorderKeyEventListener) {
            return false;
        }

        final View nextFocusedView = findNextFocus(direction);
        if (hasInBorder(direction, nextFocusedView)) {
            return mOnInBorderKeyEventListener.onInBorderKeyEvent(direction, keyCode, event);
        }
        if (null != nextFocusedView) {
            nextFocusedView.requestFocus();
        }
        return true;
    }

    /**
     * 判断选中的item是否到达边界
     */
    private boolean hasInBorder(int direction, View nextFocusedView) {
        if (null != nextFocusedView) {
            return false;
        }
        Loger.i("hasInBorder...direction=" + direction);
        switch (direction) {
            case FOCUS_DOWN:
                return !ViewCompat.canScrollVertically(this, 1);

            case FOCUS_UP:
                return !ViewCompat.canScrollVertically(this, -1);

            case FOCUS_LEFT:
                return !ViewCompat.canScrollHorizontally(this, -1);

            case FOCUS_RIGHT:

                return !ViewCompat.canScrollHorizontally(this, 1);

            default:
                return false;
        }
    }

    /**
     * 查找下个可获取焦点的view
     *
     * @param direction
     * @return
     */
    private View findNextFocus(int direction) {
        return FocusFinder.getInstance().findNextFocus(this, getFocusedChild(), direction);
    }

    /**
     * keycode值转成Direction值
     *
     * @param keyCode
     * @return
     */
    private int keyCode2Direction(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return FOCUS_DOWN;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return FOCUS_RIGHT;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return FOCUS_LEFT;
            case KeyEvent.KEYCODE_DPAD_UP:
                return FOCUS_UP;
            default:
                return -1;
        }
    }

    public void setOnInBorderKeyEventListener(TvFocusGridView.OnInBorderKeyEventListener onInBorderKeyEventListener) {
        mOnInBorderKeyEventListener = onInBorderKeyEventListener;
    }

    public interface OnInBorderKeyEventListener {
        boolean onInBorderKeyEvent(int direction, int keyCode, KeyEvent event);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (mNumColumns > 1) {
//            View view = getFocusedChild();
//            View nextFocus;
//            int childPosition = getChildLayoutPosition(view);
//            int childCount = getChildCount();
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                switch (event.getKeyCode()) {
//                    case KeyEvent.KEYCODE_DPAD_DOWN:
//                        nextFocus = FocusFinder.getInstance().findNextFocus(this, getFocusedChild(), FOCUS_DOWN);
//                        int percent = childCount % mNumColumns;
//                        if (nextFocus == null && percent > 0 && childPosition < (childCount - (childCount % mNumColumns))) {
//                            View lastView = getChildAt(childCount - 1);
//                            if (lastView != null) {
//                                lastView.requestLayout();
//                                lastView.requestFocus();
//                                return true;
//                            }
//                        }
//                        break;
//                    case KeyEvent.KEYCODE_DPAD_RIGHT:
//                        nextFocus = FocusFinder.getInstance().findNextFocus(this, getFocusedChild(), FOCUS_RIGHT);
//                        if (nextFocus == null && childPosition < (childCount - 1)) {
//                            View nextView = getChildAt(childPosition + 1);
//                            if (nextView != null) {
//                                nextView.requestLayout();
//                                nextView.requestFocus();
//                                return true;
//                            }
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }
//
//    @Override
//    public void setNumColumns(int numColumns) {
//        mNumColumns = numColumns;
//        super.setNumColumns(numColumns);
//    }
}
