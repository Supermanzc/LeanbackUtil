package com.wt.leanbackutil.leankback.holder;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.open.leanback.widget.Presenter;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyan
 * singer holder
 */

public class WheelConcertHolder extends Presenter.ViewHolder{

    @BindView(R.id.frame_img)
    FrameLayout frameLayout;
    @BindView(R.id.img_view)
    public SimpleDraweeView imageView;
    @BindView(R.id.title_view)
    public TextView titleView;
    @BindView(R.id.description_view)
    public TextView descriptionView;

    public int type;

    public WheelConcertHolder(View itemView, Context context, int type) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.type = type;
        LinearLayout.LayoutParams descriptionLayoutParams;
        LinearLayout.LayoutParams titleLayoutParams;
        switch (type) {
            case RecommendInfo.TYPE_ONE:
                frameLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_860),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_400)));
                titleView.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                break;
            case RecommendInfo.TYPE_TWO:
                frameLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_420),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_195)));
                titleView.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                descriptionLayoutParams = (LinearLayout.LayoutParams) descriptionView.getLayoutParams();
                descriptionLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_273);
                descriptionView.setLayoutParams(descriptionLayoutParams);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            case RecommendInfo.TYPE_THREE:
                frameLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_420),
                        context.getResources().getDimensionPixelOffset(R.dimen.h_240)));
                descriptionLayoutParams = (LinearLayout.LayoutParams) descriptionView.getLayoutParams();
                descriptionLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_420);
                titleLayoutParams = (LinearLayout.LayoutParams) titleView.getLayoutParams();
                titleLayoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.w_420);
                titleView.setLayoutParams(titleLayoutParams);
                descriptionView.setLayoutParams(descriptionLayoutParams);
                titleView.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                break;
            case RecommendInfo.TYPE_FOUR:
                frameLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.w_274),
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
        ViewUtils.onFocus(imageView);
    }
}
