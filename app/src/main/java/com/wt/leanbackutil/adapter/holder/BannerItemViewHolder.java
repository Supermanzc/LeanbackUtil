package com.wt.leanbackutil.adapter.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ViewUtils;
import com.zhouwei.mzbanner.holder.MZViewHolder;

/**
 * Created by DELL on 2018/8/13.
 */

public class BannerItemViewHolder implements MZViewHolder<SingItem> {

    private ImageView mImageView;

    @Override
    public View createView(final Context context) {
        LogUtil.d("createView--------------");
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner_view, null);
        mImageView = view.findViewById(R.id.banner_image);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ScaleAnimation animation = (ScaleAnimation) AnimationUtils.loadAnimation(context, R.anim.scale_in);
                    v.clearAnimation();
                    v.startAnimation(animation);
                } else {
                    ScaleAnimation animation = (ScaleAnimation) AnimationUtils.loadAnimation(context, R.anim.scale_out);
                    v.clearAnimation();
                    v.startAnimation(animation);
                }
            }
        });
        return view;
    }

    @Override
    public void onBind(Context context, int position, SingItem data) {
        Glide.with(context).load(data.getPic()).into(mImageView);
    }
}
