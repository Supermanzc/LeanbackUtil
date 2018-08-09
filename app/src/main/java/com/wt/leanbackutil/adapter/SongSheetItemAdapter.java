package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wt.leanbackutil.fragment.HomeLoadMoreFragment;
import com.wt.leanbackutil.model.SongSheetItem;

import java.util.List;

/**
 * Created by DELL on 2018/8/9.
 */

public class SongSheetItemAdapter extends BaseAdapter<List<SongSheetItem>, HomeLoadMoreFragment> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
