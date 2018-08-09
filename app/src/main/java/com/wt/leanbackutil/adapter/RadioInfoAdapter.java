package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RadioInfoOneHolder;
import com.wt.leanbackutil.adapter.holder.RadioInfoTwoHolder;
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
    private int mNumLine = 5;

    private static final int VIEW_TYPE_ONE = 0;
    private static final int VIEW_TYPE_TWO = 1;

    public RadioInfoAdapter(HomeRadioFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_ONE) {
            view = View.inflate(parent.getContext(), R.layout.item_radio_info_one, null);
            holder = new RadioInfoOneHolder(view);
        } else if (viewType == VIEW_TYPE_TWO) {
            view = View.inflate(parent.getContext(), R.layout.item_radio_info_two, null);
            holder = new RadioInfoTwoHolder(view);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_radio_info_one, null);
            holder = new RadioInfoOneHolder(view);
        }
//        View view = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.item_radio_info, null, false);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioInfo radioInfo = radioInfos.get(position);
        RadioItemAdapter radioItemAdapter;
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ONE:
                RadioInfoOneHolder radioInfoHolder = (RadioInfoOneHolder) holder;
                radioInfoHolder.titleView.setText(radioInfo.getRadio_group_name());
                radioInfoHolder.verticalGridView.setNumColumns(mNumLine);
                radioInfoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
                radioInfoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
                radioInfoHolder.verticalGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);

                radioItemAdapter = new RadioItemAdapter(mFragment);
                radioItemAdapter.setData(radioInfo.getRadios());
                radioInfoHolder.verticalGridView.setAdapter(radioItemAdapter);
                break;
            case VIEW_TYPE_TWO:
                RadioInfoTwoHolder radioInfoTwoHolder = (RadioInfoTwoHolder) holder;
                radioInfoTwoHolder.verticalGridView.setNumColumns(mNumLine);
                radioInfoTwoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
                radioInfoTwoHolder.verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
                radioInfoTwoHolder.verticalGridView.getBaseGridViewLayoutManager().setAutoMeasureEnabled(true);

                radioItemAdapter = new RadioItemAdapter(mFragment);
                radioItemAdapter.setData(radioInfo.getRadios());
                radioInfoTwoHolder.verticalGridView.setAdapter(radioItemAdapter);
                break;
            default:
                break;
        }


//        int top = 50;
//        int right = 60;
//        radioInfoHolder.verticalGridView.addItemDecoration(new SpaceItemDecoration(right, top));

        //对VerticalGridView 高度的测量
//        int line = (radioInfo.getRadios().size() + mNumLine - 1) / mNumLine;
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) radioInfoHolder.verticalGridView.getLayoutParams();
//        layoutParams.height = line * mFragment.getResources().getDimensionPixelOffset(R.dimen.h_368) - mFragment.getResources().getDimensionPixelOffset(R.dimen.h_50);
//        radioInfoHolder.verticalGridView.setLayoutParams(layoutParams);
//        radioInfoHolder.verticalGridView.setVerticalMargin(mFragment.getResources().getDimensionPixelOffset(R.dimen.h_50));
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
