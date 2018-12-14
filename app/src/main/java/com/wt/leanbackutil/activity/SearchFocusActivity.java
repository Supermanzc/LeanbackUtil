package com.wt.leanbackutil.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wt.leanbackutil.R;

import butterknife.ButterKnife;

/**
 * @author junyan
 *         焦点查找相关测试类
 */

public class SearchFocusActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_focus_activity);
        ButterKnife.bind(this);
    }
}
