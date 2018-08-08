package com.wt.leanbackutil.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.RecommendInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/8.
 */

public class RecommendSingHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_view)
    ImageView imageView;
    @BindView(R.id.title_view)
    TextView titleView;
    @BindView(R.id.description_view)
    TextView descriptionView;

    public RecommendSingHolder(View itemView, Context context, int type) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        switch (type) {
            case RecommendInfo.TYPE_ONE:
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_975),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_390)));
                titleView.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                break;
            case RecommendInfo.TYPE_TWO:
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_273),
                        context.getResources().getDimensionPixelOffset(R.dimen.w_273)));
                titleView.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            case RecommendInfo.TYPE_THREE:
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_480),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_274)));
                titleView.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            case RecommendInfo.TYPE_FOUR:
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_274),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_274)));
                titleView.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
