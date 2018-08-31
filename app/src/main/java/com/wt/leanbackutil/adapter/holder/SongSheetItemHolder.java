package com.wt.leanbackutil.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.BaseHolder;

import butterknife.BindView;

/**
 * Created by DELL on 2018/8/9.
 */

public class SongSheetItemHolder extends BaseHolder {

    @BindView(R.id.img_view)
    public SimpleDraweeView imageView;
    @BindView(R.id.title_view)
    public TextView titleView;

    public SongSheetItemHolder(View itemView) {
        super(itemView);
        initFocus(imageView);
    }
}
