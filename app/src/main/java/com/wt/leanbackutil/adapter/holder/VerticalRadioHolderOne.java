package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.open.leanback.widget.HorizontalGridView;
import com.wt.leanbackutil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyuan
 *
 */

public class VerticalRadioHolderOne extends RecyclerView.ViewHolder{

    @BindView(R.id.title)
    public TextView textView;
    @BindView(R.id.horizontal_grid_view)
    public HorizontalGridView horizontalGridView;

    public VerticalRadioHolderOne(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
