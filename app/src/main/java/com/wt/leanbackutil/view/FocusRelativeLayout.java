package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * @author junyan
 */

public class FocusRelativeLayout extends RelativeLayout {

    private View focusedView;

    public FocusRelativeLayout(Context context) {
        super(context);
    }

    public FocusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
