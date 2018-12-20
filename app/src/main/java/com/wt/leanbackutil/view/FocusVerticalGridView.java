package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.open.leanback.widget.VerticalGridView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author junyan
 * 焦点自动下移
 */
public class FocusVerticalGridView extends VerticalGridView {

    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(2);

    public FocusVerticalGridView(Context context) {
        this(context, null);
    }

    public static void onExecutorServiceSubmit(int keyCode) {
        mExecutorService.submit(new ServiceRunnable(keyCode));
    }

    public FocusVerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        View focusView = super.focusSearch(focused, direction);
        if ((focusView == null) && (direction == View.FOCUS_RIGHT)) {
            // 到达最右边，焦点下移.
            onExecutorServiceSubmit(KeyEvent.KEYCODE_DPAD_DOWN);
        } else if ((focusView == null) && (direction == View.FOCUS_LEFT)) {
            // 到达最左边，焦点下移.
            onExecutorServiceSubmit(KeyEvent.KEYCODE_DPAD_UP);
        }
        return focusView;
    }

    @Override
    public boolean onRequestFocusInDescendants(int direction, Rect paramRect) {
//
//        if ((getChildCount() <= 0) || (paramRect == null)) {
//            return false;
//        }
//
//        if (direction == View.FOCUS_FORWARD) {
////            direction == 66;
//        }
//        int n = 0;
//        Rect localObject4 = new Rect();
//        getChildAt(n).getFocusedRect((Rect)localObject4);
//        offsetDescendantRectToMyCoords(getChildAt(n), (Rect)localObject4);
////        getChildAt(3).requestFocus(66, paramRect);
//        return false;
        return super.onRequestFocusInDescendants(direction, paramRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
    }

    public int a(Rect paramRect1, Rect paramRect2)
    {
        return (int)(Math.pow(paramRect1.left + (paramRect1.right - paramRect1.left) / 2 - (paramRect2.left + (paramRect2.right - paramRect2.left) / 2), 2.0D) + Math.pow(paramRect1.top + (paramRect1.bottom - paramRect1.top) / 2 - (paramRect2.top + (paramRect2.bottom - paramRect2.top) / 2), 2.0D));
    }

    boolean a(Rect paramRect1, Rect paramRect2, int direction)
    {
        switch (direction)
        {
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            case View.FOCUS_LEFT: //17:
                if (((paramRect1.right <= paramRect2.right) && (paramRect1.left < paramRect2.right)) || (paramRect1.left <= paramRect2.left))
                    break;
            case  View.FOCUS_RIGHT: //66:
            case View.FOCUS_UP:// 33:
            case View.FOCUS_DOWN: //130:
        }
        if (((paramRect1.left < paramRect2.left) || (paramRect1.right <= paramRect2.left)) && (paramRect1.right < paramRect2.right)) {
            return false;
        } else if (((paramRect1.bottom > paramRect2.bottom) || (paramRect1.top >= paramRect2.bottom)) && (paramRect1.top > paramRect2.top)) {
            return false;
        } else if (((paramRect1.top < paramRect2.top) || (paramRect1.bottom <= paramRect2.top)) && (paramRect1.bottom < paramRect2.bottom)) {
            return false;
        }
        return true;
    }

}
