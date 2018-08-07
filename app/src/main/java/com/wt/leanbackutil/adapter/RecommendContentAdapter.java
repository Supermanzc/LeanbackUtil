package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.model.Card;
import com.wt.leanbackutil.model.RecommendInfo;

import java.util.List;

/**
 * Created by DELL on 2018/8/7.
 * 推荐位内容填充
 */

public class RecommendContentAdapter extends RecyclerView.Adapter {

    private HomeRecommendFragment mFragment;
    private List<RecommendInfo> recommendInfos;

    public RecommendContentAdapter(HomeRecommendFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
