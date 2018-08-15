package com.wt.leanbackutil.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import com.open.leanback.widget.VerticalGridView;

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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mNumColumns > 1) {
            View view = getFocusedChild();
            View nextFocus;
            int childPosition = getChildLayoutPosition(view);
            int childCount = getChildCount();
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        nextFocus = FocusFinder.getInstance().findNextFocus(this, getFocusedChild(), FOCUS_DOWN);
                        int percent = childCount % mNumColumns;
                        if (nextFocus == null && percent > 0 && childPosition < (childCount - (childCount % mNumColumns))) {
                            View lastView = getChildAt(childCount - 1);
                            if (lastView != null) {
                                lastView.requestLayout();
                                lastView.requestFocus();
                                return true;
                            }
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        nextFocus = FocusFinder.getInstance().findNextFocus(this, getFocusedChild(), FOCUS_RIGHT);
                        if (nextFocus == null && childPosition < (childCount - 1)) {
                            View nextView = getChildAt(childPosition + 1);
                            if (nextView != null) {
                                nextView.requestLayout();
                                nextView.requestFocus();
                                return true;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
        super.setNumColumns(numColumns);
    }
}
