package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

import com.open.leanback.widget.HorizontalGridView;

/**
 * Created by DELL on 2018/8/29.
 */

public class TvHorizontalGridView extends HorizontalGridView {

    public TvHorizontalGridView(Context context) {
        super(context);
    }

    public TvHorizontalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvHorizontalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public void onScrollStateChanged(int state) {
//        if (state == SCROLL_STATE_IDLE) {
//            setScrollValue(0, 0);
//        }
//        super.onScrollStateChanged(state);
//    }
//
//    @Override
//    public void smoothScrollBy(int dx, int dy) {
//        setScrollValue(dx, dy);
//        super.smoothScrollBy(dx, dy);
//    }
//
//    private Point mScrollPoint = new Point();
//
//    void setScrollValue(int x, int y) {
//        if (x != 0 || y != 0) {
//            mScrollPoint.set(x, y);
//            setTag(mScrollPoint);
//        } else {
//            setTag(null);
//        }
//    }
}
