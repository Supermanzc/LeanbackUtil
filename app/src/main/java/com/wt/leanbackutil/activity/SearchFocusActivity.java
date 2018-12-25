package com.wt.leanbackutil.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.wt.leanbackutil.R;

import butterknife.ButterKnife;

/**
 * @author junyan
 *         焦点查找相关测试类
 *         焦点速度控制
 */

public class SearchFocusActivity extends Activity {

    private final static long KEY_OUT_TIME = 150L;
    long mTimeLast = 0;
    long mTimeDelay = 0;
    long mTimeSpace = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_focus_activity);
        ButterKnife.bind(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            long nowTime = System.currentTimeMillis();
            this.mTimeDelay = nowTime - this.mTimeLast;
            this.mTimeLast = nowTime;
            if (this.mTimeSpace <= KEY_OUT_TIME && this.mTimeDelay <= KEY_OUT_TIME) {
                this.mTimeSpace += this.mTimeDelay;
                return true;
            }
            this.mTimeSpace = 0L;
        }
        return super.dispatchKeyEvent(event);
    }
}
