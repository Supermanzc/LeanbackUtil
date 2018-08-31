package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.BaseGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.SongListAdapter;
import com.wt.leanbackutil.model.RadioResponse;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.view.TvFocusGridView;

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

        SongListAdapter songListAdapter = new SongListAdapter(radioResponse.getData(), this);
        tvFocusGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        tvFocusGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        tvFocusGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);

        //设置焦点在屏幕中的位置
        tvFocusGridView.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);

        tvFocusGridView.setAdapter(songListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
