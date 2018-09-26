package com.wt.leanbackutil.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ShakeAnimatorUtil;

/**
 * Created by DELL on 2018/8/13.
 *
 * @author junyan
 *         实现到边界值抖动效果
 */

public class TvVerticalGridView extends VerticalGridView {

    public TvVerticalGridView(Context context) {
        super(context);
    }

    public TvVerticalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }
}
