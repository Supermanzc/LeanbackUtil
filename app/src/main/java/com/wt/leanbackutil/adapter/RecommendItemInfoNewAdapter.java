package com.wt.leanbackutil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.VerticalGridView;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RecommendMvInfoHolder;
import com.wt.leanbackutil.fragment.HomeRecommendNewFragment;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.view.WheelRelativeLayout;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.List;

/**
 * @author junyan
 *         新版推荐
 */

public class RecommendItemInfoNewAdapter extends RecyclerView.Adapter {

    private HomeRecommendNewFragment mFragment;
    private List<RecommendInfo> recommendInfos;

    private static final int MZ_BANNER = 1;
    private static final int NORMAL = 2;

    public RecommendItemInfoNewAdapter(HomeRecommendNewFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recommend_mv_info, null);
        RecyclerView.ViewHolder holder = new RecommendMvInfoHolder(view);
//        if (viewType == MZ_BANNER) {
//            view = View.inflate(parent.getContext(), R.layout.item_header_recommend, null);
//            holder = new BannerHolder(view);
//        } else {
//            view = View.inflate(parent.getContext(), R.layout.item_recommend_horizontal, null);
//            holder = new RecommendItemHolder(view);
//        }
//        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecommendMvInfoHolder mvInfoHolder = (RecommendMvInfoHolder) holder;
        final RecommendInfo recommendInfo = recommendInfos.get(position);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mvInfoHolder.wheelViewPager.getLayoutParams();
        List<SingItem> singItems = recommendInfo.getList();
        if (recommendInfo.getType() == RecommendInfo.TYPE_FOUR) {
            layoutParams.height = mFragment.getResources().getDimensionPixelOffset(R.dimen.h_750);

            mvInfoHolder.wheelViewPager.setPager(singItems, recommendInfo, 8,
                    mvInfoHolder.indicatorContainer, new MZHolderCreator() {
                        @Override
                        public MZViewHolder createViewHolder() {
                            return new MZViewHolder() {

                                private View view;

                                @Override
                                public View createView(Context context) {
                                    view = LayoutInflater.from(context).inflate(R.layout.wheel_pager_item_mv, null);
                                    return view;
                                }

                                @Override
                                public void onBind(Context context, int position, Object data) {
                                    TvRecyclerView verticalGridView = (TvRecyclerView) view;
                                    verticalGridView.setSpacingWithMargins(mFragment.getResources().getDimensionPixelSize(R.dimen.h_30),
                                            mFragment.getResources().getDimensionPixelSize(R.dimen.w_51));
//                                    verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(true, true);
//                                    verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
//                                    verticalGridView.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_PAGE);
//                                    verticalGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);
//                                    verticalGridView.setNumColumns(4);
                                    verticalGridView.setAdapter(new WheelItemMVAdapter(mFragment, (List<SingItem>) data));
                                }
                            };
                        }
                    });
        } else if (recommendInfo.getType() == RecommendInfo.TYPE_TWO) {
            layoutParams.height = mFragment.getResources().getDimensionPixelOffset(R.dimen.h_600);
            mvInfoHolder.wheelViewPager.setPager(singItems, recommendInfo, 8,
                    mvInfoHolder.indicatorContainer, new MZHolderCreator() {
                        @Override
                        public MZViewHolder createViewHolder() {
                            return new MZViewHolder() {
                                private View view;

                                @Override
                                public View createView(Context context) {
                                    view = LayoutInflater.from(context).inflate(R.layout.wheel_pager_item, null);
                                    return view;
                                }

                                @Override
                                public void onBind(Context context, int position, Object data) {
                                    WheelRelativeLayout wheelRelativeLayout = (WheelRelativeLayout) view;
                                    wheelRelativeLayout.setColumnAndRow(4, 2);
                                    wheelRelativeLayout.initView((List<SingItem>) data, context.getResources().getDimensionPixelOffset(R.dimen.w_420),
                                            context.getResources().getDimensionPixelOffset(R.dimen.h_240), context.getResources().getDimensionPixelOffset(R.dimen.w_20));
                                }
                            };
                        }
                    });
        } else if(recommendInfo.getType()  == RecommendInfo.TYPE_THREE){
            layoutParams.height = mFragment.getResources().getDimensionPixelOffset(R.dimen.h_600);
            mvInfoHolder.wheelViewPager.setPager(singItems, recommendInfo, 8,
                    mvInfoHolder.indicatorContainer, new MZHolderCreator() {
                        @Override
                        public MZViewHolder createViewHolder() {
                            return new MZViewHolder() {
                                @Override
                                public View createView(Context context) {
                                    return LayoutInflater.from(context).inflate(R.layout.wheel_pager_item, null);
                                }

                                @Override
                                public void onBind(Context context, int position, Object data) {

                                }
                            };
                        }
                    });
        }
        mvInfoHolder.wheelViewPager.setLayoutParams(layoutParams);
        mvInfoHolder.wheelViewPager.setCurrentItem(recommendInfo.getCurrentIndex());
        mvInfoHolder.titleView.setText(recommendInfo.getName());
    }

//    @Override
//    public int getItemViewType(int position) {
//        return recommendInfos.get(position).getType();
//    }

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
