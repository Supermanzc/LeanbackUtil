package com.open.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 */
public class NonOverlappingLinearLayout extends LinearLayout {
    public NonOverlappingLinearLayout(Context context) {
        this(context, null);
    }

    public NonOverlappingLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NonOverlappingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Avoids creating a hardware layer when animating alpha.
     */
    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
