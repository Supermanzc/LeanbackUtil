package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.ArrayObjectAdapter;
import com.open.leanback.widget.FocusHighlightHandler;
import com.open.leanback.widget.ItemBridgeAdapter;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.RadioRecycleAdapter;
import com.wt.leanbackutil.leankback.presenter.RadioCardPresenter;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.view.TvHorizontalGridView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DELL on 2018/8/17.
 *
 * @author junyan
 *         使用Leanback的模式使用api
 */

public class LeanBackFragment extends BaseFragment {

    @BindView(R.id.horizontal_grid_view)
    TvHorizontalGridView horizontalGridView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private Unbinder unbinder;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_leanback, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        initView();
        LogUtil.d("onCreateView-----------------" + LeanBackFragment.class.getSimpleName());
        return view;
    }

    private void initView() {
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.launcher_cards));
        RadioItem[] rows = new Gson().fromJson(json, RadioItem[].class);

        RadioCardPresenter radioCardPresenter = new RadioCardPresenter(getContext());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(radioCardPresenter);
        for (RadioItem radioItem : rows) {
            listRowAdapter.add(radioItem);
        }

        //桥接
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(listRowAdapter);
        itemBridgeAdapter.setFocusHighlight(new FocusHighlightHandler() {
            @Override
            public void onItemFocused(View view, boolean hasFocus) {
                LogUtil.e("onItemFocused----------------view=" + view.getClass().getSimpleName() + "   hasFocus=" + hasFocus);
            }

            @Override
            public void onInitializeView(View view) {

            }
        });
        horizontalGridView.setHorizontalMargin(getResources().getDimensionPixelOffset(R.dimen.w_20));
        horizontalGridView.setAdapter(itemBridgeAdapter);

        //recycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        RadioRecycleAdapter radioRecycleAdapter = new RadioRecycleAdapter(Arrays.asList(rows), this);
        recyclerView.setAdapter(radioRecycleAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
