package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * @author junyan
 *         添加焦点记忆功能
 */

public class FocusLinearLayout extends LinearLayout {

    private View focusedView;

    public FocusLinearLayout(Context context) {
        super(context);
        init();
    }

    public FocusLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FocusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(true);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (focusedView != null) {
            boolean result = focusedView.requestFocus(direction, previouslyFocusedRect);
            return result;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (hasFocus()) {
            focusedView = getFocusedChild();
        } else {
            if (isFocusable()) {
                views.add(this);
                return;
            }
        }
        super.addFocusables(views, direction, focusableMode);
    }
}
