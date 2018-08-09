package com.wt.leanbackutil.view.border;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by android_jy on 2018/4/20.
 */

public class MainLinearLayout extends LinearLayout {

    public MainLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public MainLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    WidgetTvViewBring mWidgetTvViewBring;

    private void init(Context context) {
        this.setChildrenDrawingOrderEnabled(true);
        mWidgetTvViewBring = new WidgetTvViewBring(this);
    }

    @Override
    public void bringChildToFront(View child) {
        mWidgetTvViewBring.bringChildToFront(this, child);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return mWidgetTvViewBring.getChildDrawingOrder(childCount, i);
    }
}
