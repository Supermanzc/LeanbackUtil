package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.SongSheetItemHolder;
import com.wt.leanbackutil.fragment.HomeLoadMoreFragment;
import com.wt.leanbackutil.model.SongSheetItem;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.List;

/**
 * Created by DELL on 2018/8/9.
 */

public class SongSheetItemAdapter extends BaseAdapter<List<SongSheetItem>, HomeLoadMoreFragment> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_song_sheet, null);
        view.setFocusable(true);
        return new SongSheetItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SongSheetItem songSheetItem = mData.get(position);
        final SongSheetItemHolder songSheetItemHolder = (SongSheetItemHolder) holder;
        songSheetItemHolder.titleView.setText(songSheetItem.getDiss_name());
        Glide.with(mContext.getActivity()).load(songSheetItem.getPic_url()).into(songSheetItemHolder.imageView);
        songSheetItemHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                songSheetItemHolder.titleView.setTextColor(mContext.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                ViewUtils.scaleView(v, hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
