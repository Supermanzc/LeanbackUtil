package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.MvItemHolder;
import com.wt.leanbackutil.adapter.holder.RadioItemHolder;
import com.wt.leanbackutil.fragment.HomeMvFragment;
import com.wt.leanbackutil.fragment.HomeRadioFragment;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 */

public class MvItemAdapter extends RecyclerView.Adapter {

    private HomeMvFragment mFragment;
    private List<RadioItem> radioItems;

    public MvItemAdapter(HomeMvFragment fragment){
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_mv_view, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new MvItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioItem radioItem = radioItems.get(position);
        final MvItemHolder mvItemHolder = (MvItemHolder) holder;
        mvItemHolder.titleView.setText(radioItem.getRadio_name());
        Glide.with(mFragment.getActivity()).load(radioItem.getRadio_pic()).into(mvItemHolder.imageView);
        mvItemHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mvItemHolder.titleView.setTextColor(mFragment.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                ViewUtils.scaleView(v, hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return radioItems.size();
    }

    public void setData(List<RadioItem> radioItems){
        this.radioItems = radioItems;
    }
}
