package com.wt.leanbackutil.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 处理焦点避免被遮挡处理
 */

public class FocusRecyclerView extends RecyclerView {

    public FocusRecyclerView(Context context) {
        super(context);
        init();
    }

    public FocusRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setHasFixedSize(true);
        setChildrenDrawingOrderEnabled(true);
        setWillNotDraw(false);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int index) {
        View focusView = getFocusedChild();
        if (focusView == null) {
            return index;
        }
        int focusIndex = indexOfChild(focusView);
        if (index < focusIndex) {
            return index;
        } else if (index < childCount - 1) {
            return focusIndex + childCount - 1 - index;
        } else {
            return focusIndex;
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        invalidate();
    }
}
