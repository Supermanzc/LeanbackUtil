package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wt.leanbackutil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/7.
 */

public class RecommendItemHolder extends RecyclerView.ViewHolder  {

    @BindView(R.id.title)
    public TextView titleView;
//    @BindView(R.id.card_grid_view)
//    public HorizontalGridView cardGridView;

    public RecommendItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
