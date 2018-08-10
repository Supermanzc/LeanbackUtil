package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.MvInfoOneHolder;
import com.wt.leanbackutil.adapter.holder.MvInfoTwoHolder;
import com.wt.leanbackutil.adapter.holder.RadioInfoOneHolder;
import com.wt.leanbackutil.adapter.holder.RadioInfoTwoHolder;
import com.wt.leanbackutil.fragment.HomeMvFragment;
import com.wt.leanbackutil.fragment.HomeRadioFragment;
import com.wt.leanbackutil.model.RadioInfo;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 *
 * @author junyan
 *         电台行adapter
 */

public class MvInfoAdapter extends RecyclerView.Adapter {

    private HomeMvFragment mFragment;
    private List<RadioInfo> radioInfos;
    private int mNumLine = 5;

    private static final int VIEW_TYPE_ONE = 0;
    private static final int VIEW_TYPE_TWO = 1;

    public MvInfoAdapter(HomeMvFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_ONE) {
            view = View.inflate(parent.getContext(), R.layout.item_mv_info_one, null);
            holder = new MvInfoOneHolder(view);
        } else if (viewType == VIEW_TYPE_TWO) {
            view = View.inflate(parent.getContext(), R.layout.item_mv_info_two, null);
            holder = new MvInfoTwoHolder(view);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_mv_info_one, null);
            holder = new MvInfoOneHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioInfo radioInfo = radioInfos.get(position);
        MvItemAdapter mvItemAdapter;
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ONE:
                MvInfoOneHolder mvInfoOneHolder = (MvInfoOneHolder) holder;
                mvInfoOneHolder.titleView.setText(radioInfo.getRadio_group_name());
                mvInfoOneHolder.verticalGridView.setNumColumns(mNumLine);
                mvInfoOneHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
                mvInfoOneHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
                mvInfoOneHolder.verticalGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);

                mvItemAdapter = new MvItemAdapter(mFragment);
                mvItemAdapter.setData(radioInfo.getRadios());
                mvInfoOneHolder.verticalGridView.setAdapter(mvItemAdapter);
                break;
            case VIEW_TYPE_TWO:
                MvInfoTwoHolder mvInfoTwoHolder = (MvInfoTwoHolder) holder;
                mvInfoTwoHolder.verticalGridView.setNumColumns(mNumLine);
                mvInfoTwoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
                mvInfoTwoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
                mvInfoTwoHolder.verticalGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);

                mvItemAdapter = new MvItemAdapter(mFragment);
                mvItemAdapter.setData(radioInfo.getRadios());
                mvInfoTwoHolder.verticalGridView.setAdapter(mvItemAdapter);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return radioInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return radioInfos.get(position).getType() == 1 ? VIEW_TYPE_ONE : VIEW_TYPE_TWO;
    }

    public void setData(List<RadioInfo> radioInfos) {
        this.radioInfos = radioInfos;
    }
}
