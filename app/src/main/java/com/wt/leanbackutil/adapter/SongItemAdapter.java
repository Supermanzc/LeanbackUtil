package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RadioItemHolder;
import com.wt.leanbackutil.adapter.holder.SongSheetItemHolder;
import com.wt.leanbackutil.fragment.HomeSongFragment;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.List;

/**
 * Created by DELL on 2018/8/31.
 */

public class SongItemAdapter extends BaseAdapter<RadioItem, HomeSongFragment> {


    public SongItemAdapter(List<RadioItem> data, HomeSongFragment context) {
        super(data, context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = getInflateView(mContext.getContext(), R.layout.item_radio_view, parent);
        inflateView.setFocusable(true);
        return new SongSheetItemHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioItem radioItem = mData.get(position);
        final SongSheetItemHolder radioItemHolder = (SongSheetItemHolder) holder;
        radioItemHolder.titleView.setText(radioItem.getRadio_name());
        FrescoUtil.getInstance().loadImage(radioItemHolder.imageView, radioItem.getRadio_pic(), FrescoUtil.TYPE_ONE);
        radioItemHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                radioItemHolder.titleView.setTextColor(mContext.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                ViewUtils.scaleView(v, hasFocus);
            }
        });
    }
}
