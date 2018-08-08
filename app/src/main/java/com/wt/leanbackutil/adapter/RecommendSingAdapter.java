package com.wt.leanbackutil.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RecommendSingHolder;
import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.ViewUtils;

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
        SingItem singItem = recommendInfo.getList().get(position);
        final RecommendSingHolder recommendSingHolder = (RecommendSingHolder) holder;
        switch (recommendInfo.getType()) {
            case RecommendInfo.TYPE_ONE:
                Glide.with(mFragment.getActivity()).load(singItem.getPic()).into(recommendSingHolder.imageView);
                break;
            case RecommendInfo.TYPE_TWO:
                Glide.with(mFragment.getActivity()).load(singItem.getPic_url()).into(recommendSingHolder.imageView);
                recommendSingHolder.descriptionView.setText(singItem.getDiss_name());
                break;
            case RecommendInfo.TYPE_THREE:
                Glide.with(mFragment.getActivity()).load(singItem.getAlbum_pic()).into(recommendSingHolder.imageView);
                recommendSingHolder.titleView.setText(singItem.getSong_name());
                recommendSingHolder.descriptionView.setText(singItem.getSinger_name());
                break;
            case RecommendInfo.TYPE_FOUR:
                Glide.with(mFragment.getActivity()).load(singItem.getMv_pic()).into(recommendSingHolder.imageView);
                recommendSingHolder.titleView.setText(singItem.getMv_title());
                recommendSingHolder.descriptionView.setText(singItem.getSinger_name());
                break;
            default:
                break;
        }

        recommendSingHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                recommendSingHolder.titleView.setTextColor(mFragment.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                recommendSingHolder.descriptionView.setTextColor(mFragment.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                ViewUtils.scaleView(v, hasFocus);
            }
        });
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
