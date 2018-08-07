package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.CardItemHolder;
import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.model.Card;

import java.util.List;

/**
 * Created by DELL on 2018/8/7.
 *
 * @author junyan
 *         卡片
 */

public class CardItemAdapter extends RecyclerView.Adapter {

    private HomeRecommendFragment mFragment;
    private List<Card> cards;

    public CardItemAdapter(HomeRecommendFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_card_view, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        CardItemHolder holder = new CardItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Card card = cards.get(position);
        final CardItemHolder cardItemHolder = (CardItemHolder) holder;
        cardItemHolder.ivTextView.setText(card.getTitle());
        if (card.getLocalImageResourceName() != null) {
            int resourceId = mFragment.getContext().getResources()
                    .getIdentifier(card.getLocalImageResourceName(),
                            "drawable", mFragment.getContext().getPackageName());
            Glide.with(mFragment.getContext())
                    .asBitmap()
                    .load(resourceId)
                    .into(cardItemHolder.ivImageView);
        }
        cardItemHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cardItemHolder.ivTextView.setTextColor(mFragment.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * 设置数据
     *
     * @param cards
     */
    public void setData(List<Card> cards) {
        this.cards = cards;
    }
}
