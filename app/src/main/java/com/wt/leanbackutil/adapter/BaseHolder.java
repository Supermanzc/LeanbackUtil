package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wt.leanbackutil.util.ViewUtils;

import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/31.
 */

public class BaseHolder extends RecyclerView.ViewHolder {

    public BaseHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initFocus(View view) {
        ViewUtils.onFocus(view);
    }
}
