package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.OnChildSelectedListener;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.RecommendItemAdapter;
import com.wt.leanbackutil.model.CardRow;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.LogUtil;

import java.util.Arrays;

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
    private RecommendItemAdapter recommendItemAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend, container, false);
        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        String titleStr = arguments.getString("title", "");
        LogUtil.e("Fragment create " + titleStr);
        initData();
        return view;
    }

    private void initData() {
        //初始化Data
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.cards_data));
        CardRow[] cardRows = new Gson().fromJson(json, CardRow[].class);
        recommendItemAdapter = new RecommendItemAdapter(this);
        recommendItemAdapter.setData(Arrays.asList(cardRows));
        verticalGridView.setAdapter(recommendItemAdapter);
        //表示当前焦点是否可以移出去
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
