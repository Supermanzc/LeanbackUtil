package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RecommendItemHolder;
import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.model.CardRow;

import java.util.List;

/**
 * Created by DELL on 2018/8/7.
 */

public class RecommendItemCardAdapter extends RecyclerView.Adapter {

    private HomeRecommendFragment mFragment;
    private List<CardRow> cardRows;

    public RecommendItemCardAdapter(HomeRecommendFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_recommend_horizontal, null);
        return new RecommendItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardRow cardRow = cardRows.get(position);
        final RecommendItemHolder recommendItemHolder = (RecommendItemHolder) holder;
        recommendItemHolder.titleView.setText(cardRow.getTitle());
        CardItemAdapter cardItemAdapter = new CardItemAdapter(mFragment);
        cardItemAdapter.setData(cardRow.getCards());
        recommendItemHolder.cardGridView.setHorizontalMargin(mFragment.getResources().getDimensionPixelOffset(R.dimen.w_60));
        recommendItemHolder.cardGridView.setAdapter(cardItemAdapter);
    }

    @Override
    public int getItemCount() {
        return cardRows.size();
    }

    /**
     * 设置数据
     *
     * @param cardRows
     */
    public void setData(List<CardRow> cardRows) {
        this.cardRows = cardRows;
    }
}
