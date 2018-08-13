package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.open.leanback.widget.HorizontalGridView;
import com.open.leanback.widget.NonOverlappingLinearLayout;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/8.
 */

public class MvInfoTwoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.vertical_grid_view)
    public RelativeLayout horizontalGridView;

    public MvInfoTwoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}