package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.decoration.SpaceItemDecoration;
import com.wt.leanbackutil.adapter.holder.RadioInfoHolder;
import com.wt.leanbackutil.fragment.HomeRadioFragment;
import com.wt.leanbackutil.model.RadioInfo;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 *
 * @author junyan
 *         电台行adapter
 */

public class RadioInfoAdapter extends RecyclerView.Adapter {

    private HomeRadioFragment mFragment;
    private List<RadioInfo> radioInfos;

    public RadioInfoAdapter(HomeRadioFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_radio_info, null);
//        View view = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.item_radio_info, null, false);
        return new RadioInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioInfo radioInfo = radioInfos.get(position);
        RadioInfoHolder radioInfoHolder = (RadioInfoHolder) holder;
        radioInfoHolder.titleView.setText(radioInfo.getRadio_group_name());

//        int top = 50;
//        int right = 60;
//        radioInfoHolder.verticalGridView.addItemDecoration(new SpaceItemDecoration(right, top));
        radioInfoHolder.verticalGridView.setNumColumns(5);
        radioInfoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        radioInfoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
        radioInfoHolder.verticalGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);

        RadioItemAdapter radioItemAdapter = new RadioItemAdapter(mFragment);
        radioItemAdapter.setData(radioInfo.getRadios());
        radioInfoHolder.verticalGridView.setAdapter(radioItemAdapter);
    }

    @Override
    public int getItemCount() {
        return radioInfos.size();
    }

    public void setData(List<RadioInfo> radioInfos) {
        this.radioInfos = radioInfos;
    }
}
