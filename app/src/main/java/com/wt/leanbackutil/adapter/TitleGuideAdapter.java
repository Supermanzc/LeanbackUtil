package com.wt.leanbackutil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.TitleGuideHolder;
import com.wt.leanbackutil.adapter.listener.AsyncFocusListener;

/**
 * Created by DELL on 2018/8/6.
 *
 * @author junyan
 *         头部导航item
 */

public class TitleGuideAdapter extends RecyclerView.Adapter {

    private int[] mData;
    private Context mContext;
    private AsyncFocusListener<Integer> mListener;

    public TitleGuideAdapter(int[] data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_grid_title_view, null);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        TitleGuideHolder titleGuideHolder = new TitleGuideHolder(view);
        return titleGuideHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int data = mData[position];
        final TitleGuideHolder titleGuideHolder = (TitleGuideHolder) holder;
        titleGuideHolder.titleView.setText(mContext.getResources().getString(data));
        titleGuideHolder.itemView.setTag(position);
        titleGuideHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                titleGuideHolder.lineView.setBackgroundColor(mContext.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.clear_color));
                titleGuideHolder.titleView.setTextColor(mContext.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                if (mListener != null) {
                    if (hasFocus) {
                        mListener.focusPosition((Integer) v.getTag());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public void setAsycFocusListener(AsyncFocusListener<Integer> listener) {
        mListener = listener;
    }
}
