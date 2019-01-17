package com.wt.leanbackutil.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RadioItemHolder;
import com.wt.leanbackutil.adapter.holder.WheelSingHolder;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 */

public class WheelItemMVAdapter extends RecyclerView.Adapter {

    private Fragment mFragment;
    private List<SingItem> singItems;

    public WheelItemMVAdapter(Fragment fragment, List<SingItem> singItems) {
        mFragment = fragment;
        this.singItems = singItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.wheel_item_coustom, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new WheelSingHolder(view, parent.getContext(), RecommendInfo.TYPE_THREE);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SingItem singItem = singItems.get(position);
        final WheelSingHolder wheelSingHolder = (WheelSingHolder) holder;

        wheelSingHolder.titleView.setText(singItem.getMv_title());
        wheelSingHolder.descriptionView.setText(singItem.getMv_desc());
        FrescoUtil.getInstance().loadImage(wheelSingHolder.imageView, singItem.getMv_pic(), FrescoUtil.TYPE_ONE);
        wheelSingHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                wheelSingHolder.titleView.setTextColor(mFragment.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                ViewUtils.scaleView(v, hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singItems.size();
    }
}
