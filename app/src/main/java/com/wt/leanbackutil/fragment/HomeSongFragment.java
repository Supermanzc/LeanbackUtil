package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.BaseGridView;
import com.owen.tvrecyclerview.TwoWayLayoutManager;
import com.owen.tvrecyclerview.widget.MetroGridLayoutManager;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.SongListAdapter;
import com.wt.leanbackutil.adapter.SongListDecorationAdapter;
import com.wt.leanbackutil.model.RadioCategory;
import com.wt.leanbackutil.model.RadioInfo;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.model.RadioResponse;
import com.wt.leanbackutil.model.RadioSubcategory;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.view.MetroTitleDecoration;
import com.wt.leanbackutil.view.TvFocusGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/31.
 */

public class HomeSongFragment extends BaseFragment {

    @BindView(R.id.tv_focus_grid_view)
    TvFocusGridView tvFocusGridView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_song, container, false);
        }
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    private void initData() {
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.radio_data));
        RadioResponse radioResponse = new Gson().fromJson(json, RadioResponse.class);
        RadioResponse radioResponse1 = getRadioResponse(radioResponse);
//        SongListAdapter songListAdapter = new SongListAdapter(radioResponse.getData(), this);

        SongListDecorationAdapter songListAdapter = new SongListDecorationAdapter(radioResponse1.getSubcategoryList(), this);
        songListAdapter.setCategoryTitles(radioResponse1.getCategoryList());

        tvFocusGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        tvFocusGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        tvFocusGridView.setNumColumns(5);
        tvFocusGridView.setVerticalMargin(getResources().getDimensionPixelOffset(R.dimen.w_20));
        tvFocusGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);

//        MetroGridLayoutManager layoutManager = new MetroGridLayoutManager
//                (TwoWayLayoutManager.Orientation.VERTICAL, 5);
//        tvFocusGridView.setLayoutManager(layoutManager);

        //设置焦点在屏幕中的位置
        tvFocusGridView.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);
//        tvFocusGridView.addItemDecoration(new MetroTitleDecoration(new MetroTitleDecoration.MetroTitleListener() {
//            @Override
//            public View getTitleView(int index, RecyclerView parent) {
//                return null;
//            }
//        }));

        tvFocusGridView.setOnInBorderKeyEventListener(new TvFocusGridView.OnInBorderKeyEventListener() {
            @Override
            public boolean onInBorderKeyEvent(int direction, int keyCode, KeyEvent event) {
                return direction == View.FOCUS_LEFT || direction == View.FOCUS_RIGHT;
            }
        });


        tvFocusGridView.setAdapter(songListAdapter);
    }

    /**
     * 数据整理
     *
     * @param response
     * @return
     */
    private RadioResponse getRadioResponse(RadioResponse response) {
        List<RadioSubcategory> subcategoryList = new ArrayList<>();
        List<RadioCategory> categoryList = new ArrayList<>();
        if (response.getData() != null && !response.getData().isEmpty()) {
            List<RadioInfo> categoryBeans = response.getData();
            for (int i = 0; i < categoryBeans.size(); i++) {
                RadioInfo radioGroup = categoryBeans.get(i);
                RadioCategory category = new RadioCategory();
                category.setTitle(radioGroup.getRadio_group_name());
                categoryList.add(category);

                List<RadioItem> topListTops = radioGroup.getRadios();
                if (topListTops != null && !topListTops.isEmpty()) {
                    for (RadioItem radiosBean : topListTops) {
                        RadioSubcategory subcategory = new RadioSubcategory();
                        subcategory.setRadio_id(radiosBean.getRadio_id());
                        subcategory.setRadio_name(radiosBean.getRadio_name());
                        subcategory.setRadio_pic(radiosBean.getRadio_pic());
                        subcategory.setListen_num(radiosBean.getListen_num());
                        subcategory.setSectionIndex(i);
                        subcategory.setRealEntity(true);

                        subcategoryList.add(subcategory);
                    }
                    if (topListTops.size() % 5 != 0) {
                        int leftCount = 5 - topListTops.size() % 5;
                        for (int j = 0; j < leftCount; j++) {
                            RadioSubcategory radioSubcategory = new RadioSubcategory();
                            radioSubcategory.setRealEntity(false);
                            radioSubcategory.setSectionIndex(i);
                            subcategoryList.add(radioSubcategory);
                        }
                    }
                }
            }
        }
        response.setData(null);
        response.setCategoryList(categoryList);
        response.setSubcategoryList(subcategoryList);
        return response;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void refreshRecyclerUi() {
        if (tvFocusGridView != null) {
            tvFocusGridView.scrollToPosition(0);
        }
    }
}
