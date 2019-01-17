package com.wt.leanbackutil.view;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.open.leanback.util.ThreadPoolManager;

import java.util.ArrayList;

import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;

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


    /**
     *
     * @param focused
     * @param direction
     * @return
     */
//    @Override
//    public View focusSearch(View focused, int direction) {
//        View focusView = super.focusSearch(focused, direction);
//        if ((focusView == null) && (direction == View.FOCUS_RIGHT)) {
//            // 到达最右边，焦点下移.(注意:建议放到Executors的Runnable里面执行哈，这里简化代码)
//            ThreadPoolManager.getInstance().execute(new Runnable() {
//                @Override
//                public void run() {
//                    new Instrumentation().sendKeyDownUpSync(KEYCODE_DPAD_DOWN);
//                }
//            });
//        } else if ((focusView == null) && (direction == View.FOCUS_LEFT)) {
//            ThreadPoolManager.getInstance().execute(new Runnable() {
//                @Override
//                public void run() {
//                    new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
//                }
//            });
//            // 到达最左边，焦点下移.
//        }
//        return focusView;
//    }
}
