package com.wt.leanbackutil.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.BaseHolder;
import com.wt.leanbackutil.view.TvFocusGridView;

import butterknife.BindView;

/**
 * Created by DELL on 2018/8/31.
 */

public class SongListHolder extends BaseHolder {

    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.tv_focus_grid_view)
    public TvFocusGridView tvFocusGridView;

    public SongListHolder(View view) {
        super(view);
    }
}
