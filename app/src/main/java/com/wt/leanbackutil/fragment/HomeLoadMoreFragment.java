package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.open.leanback.widget.OnChildSelectedListener;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.RadioInfoAdapter;
import com.wt.leanbackutil.adapter.SongSheetItemAdapter;
import com.wt.leanbackutil.model.RadioResponse;
import com.wt.leanbackutil.model.SongSheetItem;
import com.wt.leanbackutil.model.SongSheetResponse;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.view.border.MainUpView;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.mainUpView)
    MainUpView mainUpView;

    private Unbinder unbinder;
    private SongSheetItemAdapter songSheetItemAdapter;
    private SongSheetResponse songSheetResponse;

    private List<SongSheetItem> mDataAlls;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_more, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        mDataAlls = new ArrayList<>();
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.song_sheet_data));
        songSheetResponse = new Gson().fromJson(json, SongSheetResponse.class);
        mDataAlls.addAll(songSheetResponse.getData());
        songSheetItemAdapter = new SongSheetItemAdapter();
        songSheetItemAdapter.setContext(this);
        songSheetItemAdapter.setData(mDataAlls);
        songSheetItemAdapter.setMainView(mainUpView);

        verticalGridView.setNumColumns(5);
//        verticalGridView.setVerticalMargin(getResources().getDimensionPixelOffset(R.dimen.w_20));
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        verticalGridView.setAdapter(songSheetItemAdapter);

        verticalGridView.setOnLoadMoreListener(new VerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LogUtil.e("onLoadMore-----------------position=" + verticalGridView.getSelectedPosition());
                int position = verticalGridView.getSelectedPosition();
                String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.song_sheet_data));
                SongSheetResponse songResponse = new Gson().fromJson(json, SongSheetResponse.class);
                List<SongSheetItem> tempData = new ArrayList<>();
                tempData.addAll(mDataAlls);
                mDataAlls.addAll(songSheetResponse.getData().size(), songResponse.getData());
                songSheetItemAdapter.notifyItemChanged(position);
//                songSheetItemAdapter.setData(mDataAlls);
//                songSheetItemAdapter.notifyItemRangeInserted(tempData.size(), mDataAlls.size());
                verticalGridView.endMoreRefreshComplete();
            }
        });

        verticalGridView.getBaseGridViewLayoutManager().setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                LogUtil.d("onChildSelected---------------position=" + position + "   id=" + id);
            }
        });
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
