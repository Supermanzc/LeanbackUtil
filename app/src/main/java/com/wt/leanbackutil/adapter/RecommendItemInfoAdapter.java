package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RecommendItemHolder;
import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.model.RecommendInfo;

import java.util.List;

/**
 * Created by DELL on 2018/8/7.
 */

public class RecommendItemInfoAdapter extends RecyclerView.Adapter {

    private HomeRecommendFragment mFragment;
    private List<RecommendInfo> recommendInfos;

    public RecommendItemInfoAdapter(HomeRecommendFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_recommend_horizontal, null);
        return new RecommendItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecommendInfo recommendInfo = recommendInfos.get(position);
        final RecommendItemHolder recommendItemHolder = (RecommendItemHolder) holder;
        recommendItemHolder.titleView.setText(recommendInfo.getName());
        RecommendContentAdapter recommendContentAdapter = new RecommendContentAdapter(mFragment);
        recommendContentAdapter.setData(recommendInfo);
        recommendItemHolder.cardGridView.setHorizontalMargin(mFragment.getResources().getDimensionPixelOffset(R.dimen.w_60));
        recommendItemHolder.cardGridView.setAdapter(recommendContentAdapter);
    }

    @Override
    public int getItemCount() {
        return recommendInfos.size();
    }

    /**
     * 设置数据
     *
     * @param recommendInfos
     */
    public void setData(List<RecommendInfo> recommendInfos) {
        this.recommendInfos = recommendInfos;
    }
}
