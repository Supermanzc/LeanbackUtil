package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RecommendSingHolder;
import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.model.RecommendInfo;

/**
 * Created by DELL on 2018/8/7.
 * 推荐位内容填充
 */

public class RecommendSingAdapter extends RecyclerView.Adapter {

    private HomeRecommendFragment mFragment;
    private RecommendInfo recommendInfo;

    public RecommendSingAdapter(HomeRecommendFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_recommend_view, null);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        return new RecommendSingHolder(view, mFragment.getActivity(), recommendInfo.getType());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //处理点击事件
        switch (recommendInfo.getType()) {
            case RecommendInfo.TYPE_ONE:
                break;
            case RecommendInfo.TYPE_TWO:
                break;
            case RecommendInfo.TYPE_THREE:
                break;
            case RecommendInfo.TYPE_FOUR:
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return recommendInfo.getList().size();
    }

    /**
     * 设置数据
     *
     * @param recommendInfo
     */
    public void setData(RecommendInfo recommendInfo) {
        this.recommendInfo = recommendInfo;
    }
}
