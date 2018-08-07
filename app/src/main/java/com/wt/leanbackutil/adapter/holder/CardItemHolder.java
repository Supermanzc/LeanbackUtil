package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wt.leanbackutil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/7.
 */

public class CardItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_img)
    public ImageView ivImageView;
    @BindView(R.id.title)
    public TextView ivTextView;

    public CardItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
