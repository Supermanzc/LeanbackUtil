package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.HorizontalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.VerticalRadioHolderOne;
import com.wt.leanbackutil.adapter.holder.VerticalRadioHolderTwo;
import com.wt.leanbackutil.fragment.HomeVerticalGridViewFragment;
import com.wt.leanbackutil.model.RadioInfo;

import java.util.List;

/**
 * @author junyan
 *         横切面适配
 */

public class VerticalRadioInfoAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_ONE = 0;
    private static final int VIEW_TYPE_TWO = 1;

    private HomeVerticalGridViewFragment mFragment;
    private List<RadioInfo> radioInfos;

    public VerticalRadioInfoAdapter(HomeVerticalGridViewFragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_ONE) {
            holder = new VerticalRadioHolderOne(View.inflate(parent.getContext(),
                    R.layout.vertical_radio_type_one, null));
        } else {
            holder = new VerticalRadioHolderTwo(View.inflate(parent.getContext(),
                    R.layout.vertical_radio_type_two, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioInfo radioInfo = radioInfos.get(position);
        VerticalRadioAdapter radioItemAdapter = new VerticalRadioAdapter(mFragment);
        radioItemAdapter.setData(radioInfo.getRadios());
        if (holder.getItemViewType() == VIEW_TYPE_ONE) {
            VerticalRadioHolderOne verticalRadioHolderOne = (VerticalRadioHolderOne) holder;
            verticalRadioHolderOne.textView.setText(radioInfo.getRadio_group_name());
            verticalRadioHolderOne.horizontalGridView.setHorizontalMargin(mFragment.getResources().getDimensionPixelOffset(R.dimen.w_40));
//            verticalRadioHolderOne.horizontalGridView.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
            verticalRadioHolderOne.horizontalGridView.setAdapter(radioItemAdapter);
        } else {
            VerticalRadioHolderTwo holderTwo = (VerticalRadioHolderTwo) holder;
            holderTwo.horizontalGridView.setHorizontalMargin(mFragment.getResources().getDimensionPixelOffset(R.dimen.w_40));
//            holderTwo.horizontalGridView.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
            holderTwo.horizontalGridView.setAdapter(radioItemAdapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return radioInfos.get(position).getType() == 1 ? VIEW_TYPE_ONE : VIEW_TYPE_TWO;
    }

    @Override
    public int getItemCount() {
        return radioInfos.size();
    }

    public void setData(List<RadioInfo> radioInfos) {
        this.radioInfos = radioInfos;
    }
}
