package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.VerticalRadioInfoAdapter;
import com.wt.leanbackutil.model.RadioInfo;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.model.RadioResponse;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.PagerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyan
 *         专业处理title和内容的关系
 */

public class HomeVerticalGridViewFragment extends BaseFragment {

    @BindView(R.id.vertical_grid_view)
    VerticalGridView verticalGridView;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_radio, container, false);
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.radio_data));
        RadioResponse radioResponse = new Gson().fromJson(json, RadioResponse.class);
        VerticalRadioInfoAdapter verticalRadioInfoAdapter = new VerticalRadioInfoAdapter(this);
        verticalRadioInfoAdapter.setData(getRadioInfos(radioResponse));
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        verticalGridView.setVerticalMargin(getResources().getDimensionPixelOffset(R.dimen.h_20));
        verticalGridView.setAdapter(verticalRadioInfoAdapter);
    }

    @Override
    public void refreshRecyclerUi() {
        if (verticalGridView != null) {
            verticalGridView.scrollToPosition(0);
        }
    }

    /**
     * 数据整理
     * @param radioResponse
     * @return
     */
    private List<RadioInfo> getRadioInfos(RadioResponse radioResponse) {
        List<RadioInfo> radioInfos = new ArrayList<>();
        for (int i = 0; i < radioResponse.getData().size(); i++) {
            RadioInfo radioInfo = radioResponse.getData().get(i);
            PagerUtil<RadioItem> pagerUtil = PagerUtil.create(radioInfo.getRadios(), 5);
            for (int j = 1; j <= pagerUtil.getTotalPage(); j++) {
                RadioInfo copeRadioInfo = new RadioInfo();
                copeRadioInfo.setId(radioInfo.getId());
                copeRadioInfo.setRadio_group_name(radioInfo.getRadio_group_name());
                if (j == 1) {
                    copeRadioInfo.setType(1);
                } else {
                    copeRadioInfo.setType(0);
                }
                copeRadioInfo.setRadios(pagerUtil.getPagedList(j));
                if (copeRadioInfo != null) {
                    radioInfos.add(copeRadioInfo);
                }
            }
        }
        return radioInfos;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
