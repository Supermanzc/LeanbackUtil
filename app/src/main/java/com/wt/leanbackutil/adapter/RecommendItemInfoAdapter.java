package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
        RecommendSingAdapter recommendSingAdapter = new RecommendSingAdapter(mFragment);
        recommendSingAdapter.setData(recommendInfo);
        int margin;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recommendItemHolder.cardGridView.getLayoutParams();
        switch (recommendInfo.getType()) {
            case RecommendInfo.TYPE_ONE:
                margin = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_30);
                layoutParams.height = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_390);
                break;
            case RecommendInfo.TYPE_TWO:
                margin = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_40);
                layoutParams.height = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_306);
                break;
            case RecommendInfo.TYPE_THREE:
                margin = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_50);
                layoutParams.height = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_340);
                break;
            case RecommendInfo.TYPE_FOUR:
                margin = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_60);
                layoutParams.height = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_340);
                break;
            default:
                margin = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_20);
                break;
        }
        recommendItemHolder.cardGridView.setLayoutParams(layoutParams);
        recommendItemHolder.cardGridView.setHorizontalMargin(margin);
        recommendItemHolder.cardGridView.setAdapter(recommendSingAdapter);
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
