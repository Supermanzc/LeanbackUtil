package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.open.leanback.widget.BaseGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.MvInfoAdapter;
import com.wt.leanbackutil.model.RadioInfo;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.model.RadioResponse;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.PagerUtil;
import com.wt.leanbackutil.view.TvVerticalGridView;
import com.wt.leanbackutil.view.border.MainUpView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/8.
 *
 * @author junyan
 *         电台
 */

public class HomeMvFragment extends BaseFragment {

    @BindView(R.id.vertical_grid_view)
    TvVerticalGridView verticalGridView;
    @BindView(R.id.main_up_view)
    MainUpView mainUpView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mv_radio, container, false);
        }
        ButterKnife.bind(this, view);

        initData();
        LogUtil.d("onCreateView-----------------" + HomeMvFragment.class.getSimpleName());
        return view;
    }

    private void initData() {
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.radio_data));
        RadioResponse radioResponse = new Gson().fromJson(json, RadioResponse.class);
        MvInfoAdapter mvInfoAdapter = new MvInfoAdapter(this);
        mvInfoAdapter.setData(getRadioInfos(radioResponse));
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);

        //设置焦点在屏幕中的位置
        verticalGridView.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);

        verticalGridView.setAdapter(mvInfoAdapter);
        verticalGridView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                LogUtil.e("------------------v=" + v.getClass().getSimpleName());
                LogUtil.e("------------------keyCode=" + keyCode);
                LogUtil.e("------------------getKeyCode=" + event.getKeyCode());
                return false;
            }
        });
        verticalGridView.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
//                LogUtil.e("------------event------getAction=" + event.getAction());
//                LogUtil.e("------------event------getKeyCode=" + event.getKeyCode());
//                if(event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
//                    View focusedChild = verticalGridView.getFocusedChild();
//                    LogUtil.e("focusedChild--------" + focusedChild);
//                    View childAt = verticalGridView.getChildAt(0);
//                    if(focusedChild != null && focusedChild == childAt){
//                        LogUtil.e("getChildAt--------" + childAt);
//                        if(childAt instanceof RelativeLayout){
//                            RelativeLayout viewGroup = (RelativeLayout) ((RelativeLayout) childAt).getChildAt(1);
//                            viewGroup.getChildAt(0).requestFocus();
//                            viewGroup.getChildAt(0).requestLayout();
//                        }
//                    }
//                }
                return false;
            }
        });

        verticalGridView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtil.e("v---------------------" + v.getClass().getSimpleName() + "   hasFocus=" + hasFocus);
            }
        });
    }

    @Override
    public void refreshRecyclerUi() {
        if (verticalGridView != null) {
            verticalGridView.scrollToPosition(0);
        }
    }

    /**
     * 数据整理
     *
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

    public void setOnFocusChange(final View v, boolean hasFocus) {
//        if (hasFocus) {
//            v.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mainUpView.setFocusView(v, 1.2f);
//                }
//            }, 200);
//        } else {
//            mainUpView.setFocusView(v, 1.0f);
//        }
    }
}
