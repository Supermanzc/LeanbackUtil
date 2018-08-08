package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.model.RecommendInfo;

/**
 * Created by DELL on 2018/8/7.
 * 推荐位内容填充
 */

public class RecommendContentAdapter extends RecyclerView.Adapter {

    private HomeRecommendFragment mFragment;
    private RecommendInfo recommendInfo;

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
        return recommendInfo.getList().size();
    }

    /**
     * 设置数据
     *
     * @param recommendInfos
     */
    public void setData(RecommendInfo recommendInfos) {
        this.recommendInfo = recommendInfos;
    }
}
