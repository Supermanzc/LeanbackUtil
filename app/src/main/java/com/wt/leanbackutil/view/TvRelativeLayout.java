package com.wt.leanbackutil.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ShakeAnimatorUtil;

/**
 * Created by DELL on 2018/8/21.
 */

public class TvRelativeLayout extends RelativeLayout {

    public TvRelativeLayout(Context context) {
        super(context);
    }

    public TvRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            View view = getFocusedChild();
            if(ShakeAnimatorUtil.getInstance().isNeedShake(this, view, event)){
                ShakeAnimatorUtil.getInstance().setHorizontalShakeAnimator(view);
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
