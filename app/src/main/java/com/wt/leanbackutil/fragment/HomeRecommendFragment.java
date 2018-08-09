package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.RecommendItemCardAdapter;
import com.wt.leanbackutil.adapter.RecommendItemInfoAdapter;
import com.wt.leanbackutil.model.RecommendResponse;
import com.wt.leanbackutil.util.FileJsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/6.
 *
 * @author junyan
 *         推荐
 */

public class HomeRecommendFragment extends BaseFragment {

    @BindView(R.id.vertical_grid_view)
    VerticalGridView verticalGridView;
    private RecommendItemCardAdapter recommendItemAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        //初始化Data
//        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.cards_data));
//        CardRow[] cardRows = new Gson().fromJson(json, CardRow[].class);
//        recommendItemAdapter = new RecommendItemCardAdapter(this);
//        recommendItemAdapter.setData(Arrays.asList(cardRows));
//        verticalGridView.setAdapter(recommendItemAdapter);

        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.recommend_data));
        RecommendResponse recommendResponse = new Gson().fromJson(json, RecommendResponse.class);
        RecommendItemInfoAdapter recommendItemInfoAdapter = new RecommendItemInfoAdapter(this);
        recommendItemInfoAdapter.setData(recommendResponse.getData().getReco_datas());
        verticalGridView.setAdapter(recommendItemInfoAdapter);

        //表示当前焦点是否可以移出去
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        verticalGridView.setVerticalMargin(getResources().getDimensionPixelOffset(R.dimen.h_20));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void refreshRecyclerUi() {
        if (verticalGridView != null) {
            verticalGridView.scrollToPosition(0);
        }
    }
}
