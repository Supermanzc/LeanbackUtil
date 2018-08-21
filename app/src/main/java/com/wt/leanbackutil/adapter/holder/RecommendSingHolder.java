package com.wt.leanbackutil.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.RecommendInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/8.
 */

public class RecommendSingHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_view)
    public SimpleDraweeView imageView;
    @BindView(R.id.title_view)
    public TextView titleView;
    @BindView(R.id.description_view)
    public TextView descriptionView;

    public RecommendSingHolder(View itemView, Context context, int type) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        LinearLayout.LayoutParams descriptionLayoutParams;
        LinearLayout.LayoutParams titleLayoutParams;
        switch (type) {
            case RecommendInfo.TYPE_ONE:
                imageView.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_975),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_390)));
                titleView.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                break;
            case RecommendInfo.TYPE_TWO:
                imageView.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_273),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_273)));
                titleView.setVisibility(View.GONE);
                descriptionLayoutParams = (LinearLayout.LayoutParams) descriptionView.getLayoutParams();
                descriptionLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_273);
                descriptionView.setLayoutParams(descriptionLayoutParams);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            case RecommendInfo.TYPE_THREE:
                imageView.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_480),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_274)));
                descriptionLayoutParams = (LinearLayout.LayoutParams) descriptionView.getLayoutParams();
                descriptionLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_480);
                titleLayoutParams = (LinearLayout.LayoutParams) titleView.getLayoutParams();
                titleLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_480);
                titleView.setLayoutParams(titleLayoutParams);
                descriptionView.setLayoutParams(descriptionLayoutParams);
                titleView.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            case RecommendInfo.TYPE_FOUR:
                imageView.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_274),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_274)));
                descriptionLayoutParams = (LinearLayout.LayoutParams) descriptionView.getLayoutParams();
                descriptionLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_274);
                titleLayoutParams = (LinearLayout.LayoutParams) titleView.getLayoutParams();
                titleLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_274);
                titleView.setLayoutParams(titleLayoutParams);
                descriptionView.setLayoutParams(descriptionLayoutParams);
                titleView.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
