package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.RecommendItemCardAdapter;
import com.wt.leanbackutil.adapter.RecommendItemInfoAdapter;
import com.wt.leanbackutil.adapter.RecommendItemInfoNewAdapter;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.model.RecommendResponse;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/6.
 *
 * @author junyan
 *         推荐
 */

public class HomeRecommendNewFragment extends BaseFragment {

    @BindView(R.id.vertical_grid_view)
    VerticalGridView verticalGridView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_recommend, container, false);
        }
        ButterKnife.bind(this, view);
        initData();
        LogUtil.d("onCreateView-----------------" + HomeRecommendNewFragment.class.getSimpleName());
        return view;
    }

    private void initData() {
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.recommend_data));
        RecommendResponse recommendResponse = new Gson().fromJson(json, RecommendResponse.class);

        RecommendItemInfoNewAdapter recommendItemInfoAdapter = new RecommendItemInfoNewAdapter(this);
        List<RecommendInfo> recommendInfos = recommendResponse.getData().getReco_datas().subList(1, recommendResponse.getData().getReco_datas().size());

//        recommendItemInfoAdapter.setData(recommendResponse.getData().getReco_datas());
        recommendItemInfoAdapter.setData(recommendInfos);

        //添加headerView
//        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_header_recommend, verticalGridView, false);

        verticalGridView.setAdapter(recommendItemInfoAdapter);

        //表示当前焦点是否可以移出去
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        verticalGridView.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);
        verticalGridView.setVerticalMargin(getResources().getDimensionPixelOffset(R.dimen.h_20));
    }

    @Override
    public void refreshRecyclerUi() {
        if (verticalGridView != null) {
            verticalGridView.scrollToPosition(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
