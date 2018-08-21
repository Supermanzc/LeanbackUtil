package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.view.TvRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/8.
 */

public class MvInfoOneHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView titleView;
    @BindView(R.id.vertical_grid_view)
    public TvRelativeLayout horizontalGridView;

    public MvInfoOneHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
