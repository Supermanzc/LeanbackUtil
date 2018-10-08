package com.open.leanback.bring;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author
 * 放大之后避免被遮挡处理
 */
public class BringToFrontLinear extends LinearLayout {

    public BringToFrontLinear(Context context) {
        super(context);
        mInit();
    }

    public BringToFrontLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInit();
    }

    public BringToFrontLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit();
    }

    public BringToFrontLinear(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mInit();
    }

    private BringToFrontHelper bringToFrontHelper;

    private void mInit() {
        setWillNotDraw(true);
        setChildrenDrawingOrderEnabled(true);
        bringToFrontHelper = new BringToFrontHelper();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return bringToFrontHelper.getChildDrawingOrder(childCount,i);
    }

    @Override
    public void bringChildToFront(View child) {
        bringToFrontHelper.bringChildToFront(this,child);
    }
}
