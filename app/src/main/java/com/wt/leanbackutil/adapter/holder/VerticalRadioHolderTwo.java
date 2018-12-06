package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.open.leanback.widget.HorizontalGridView;
import com.wt.leanbackutil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyuan
 */

public class VerticalRadioHolderTwo extends RecyclerView.ViewHolder {

    @BindView(R.id.horizontal_grid_view)
    public HorizontalGridView horizontalGridView;

    public VerticalRadioHolderTwo(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
