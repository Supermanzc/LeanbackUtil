package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.open.leanback.widget.GridLayoutManager;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.SongListHolder;
import com.wt.leanbackutil.fragment.HomeSongFragment;
import com.wt.leanbackutil.model.RadioInfo;

import java.util.List;

/**
 * Created by DELL on 2018/8/31.
 *
 * @author junyan
 *         歌曲列表
 */

public class SongListAdapter extends BaseAdapter<RadioInfo, HomeSongFragment> {

    public SongListAdapter(List<RadioInfo> data, HomeSongFragment homeSongFragment) {
        super(data, homeSongFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongListHolder(getInflateView(mContext.getContext(), R.layout.item_song_list, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioInfo radioInfo = mData.get(position);
        SongListHolder songListHolder = (SongListHolder) holder;
        songListHolder.mTitle.setText(radioInfo.getRadio_group_name());
        songListHolder.tvFocusGridView.setNumColumns(5);
        songListHolder.tvFocusGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);
        songListHolder.tvFocusGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        GridLayoutManager gridLayoutManager = songListHolder.tvFocusGridView.getBaseGridViewLayoutManager();
//        gridLayoutManager.generateLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.h_318) * (radioInfo.getRadios().size() + 4) / 5;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) songListHolder.tvFocusGridView.getLayoutParams();
        layoutParams.height = height;
        songListHolder.tvFocusGridView.setAdapter(new SongItemAdapter(radioInfo.getRadios(), mContext));
    }
}
