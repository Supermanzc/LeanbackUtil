package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.view.WheelViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyan
 * 推荐mv信息
 */

public class RecommendMvInfoHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.indicator_container)
    public LinearLayout indicatorContainer;
    @BindView(R.id.wheel_view_pager)
    public WheelViewPager<SingItem> wheelViewPager;
    @BindView(R.id.title)
    public TextView titleView;

    public RecommendMvInfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
