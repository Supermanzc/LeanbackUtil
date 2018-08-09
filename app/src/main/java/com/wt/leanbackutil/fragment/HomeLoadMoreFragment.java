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
import com.wt.leanbackutil.adapter.RadioInfoAdapter;
import com.wt.leanbackutil.adapter.SongSheetItemAdapter;
import com.wt.leanbackutil.model.RadioResponse;
import com.wt.leanbackutil.model.SongSheetResponse;
import com.wt.leanbackutil.util.FileJsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DELL on 2018/8/9.
 *
 * @author junyan
 *         加载更多
 */

public class HomeLoadMoreFragment extends BaseFragment {

    @BindView(R.id.vertical_grid_view)
    VerticalGridView verticalGridView;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_more, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.song_sheet_data));
        SongSheetResponse songSheetResponse = new Gson().fromJson(json, SongSheetResponse.class);
        SongSheetItemAdapter songSheetItemAdapter = new SongSheetItemAdapter();
        songSheetItemAdapter.setContext(this);
        songSheetItemAdapter.setData(songSheetResponse.getData());

        verticalGridView.setNumColumns(5);
        verticalGridView.setVerticalMargin(getResources().getDimensionPixelOffset(R.dimen.w_20));
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        verticalGridView.setAdapter(songSheetItemAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refreshRecyclerUi() {
        if (verticalGridView != null) {
            verticalGridView.scrollToPosition(0);
        }
    }
}
