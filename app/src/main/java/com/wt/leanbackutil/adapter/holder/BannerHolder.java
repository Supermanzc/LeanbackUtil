package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wt.leanbackutil.R;
import com.zhouwei.mzbanner.MZBannerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/13.
 */

public class BannerHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.banner_mz_header)
    public MZBannerView bannerView;

    public BannerHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
