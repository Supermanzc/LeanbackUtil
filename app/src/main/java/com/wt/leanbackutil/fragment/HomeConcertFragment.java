package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.ArrayObjectAdapter;
import com.open.leanback.widget.HeaderItem;
import com.open.leanback.widget.ItemBridgeAdapter;
import com.open.leanback.widget.ListRow;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.leankback.presenter.ConcertPresenter;
import com.wt.leanbackutil.leankback.seletor.ConcertPresenterSelector;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.model.RecommendResponse;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyan
 *         演唱会demo
 */

public class HomeConcertFragment extends BaseFragment {

    @BindView(R.id.vertical_grid_view)
    VerticalGridView verticalGridView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_concert, container, false);
        }
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //数据配置
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.recommend_data_new));
        RecommendResponse recommendResponse = new Gson().fromJson(json, RecommendResponse.class);

        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(true, true);
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new ConcertPresenterSelector());
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(arrayObjectAdapter);
        verticalGridView.setAdapter(itemBridgeAdapter);
        //有多少种类型就有多少个item
        for (int i = 0; i < recommendResponse.getData().getReco_datas().size(); i++) {
            List<RecommendInfo> recommendInfos = recommendResponse.getData().getReco_datas();
            RecommendInfo recommendInfo = recommendInfos.get(i);

            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new ConcertPresenter(recommendInfo.getType()));
            listRowAdapter.addAll(0, recommendInfo.getList());

            HeaderItem settingHeader = new HeaderItem(i, recommendInfo.getName());
            ListRow listRow;
            if (recommendInfo.getType() == RecommendInfo.TYPE_ONE
                    || recommendInfo.getType() == RecommendInfo.TYPE_TWO
                    || recommendInfo.getType() == RecommendInfo.TYPE_SEVEN) {
                listRow = new ListRow(recommendInfo.getType(), null, listRowAdapter);
            } else {
                listRow = new ListRow(recommendInfo.getType(), settingHeader, listRowAdapter);
            }
            arrayObjectAdapter.add(listRow);
        }
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
