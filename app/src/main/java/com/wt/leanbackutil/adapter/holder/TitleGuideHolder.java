package com.wt.leanbackutil.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wt.leanbackutil.R;

/**
 * Created by DELL on 2018/8/6.
 * @author junyan
 *  头部导航view
 */

public class TitleGuideHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public View lineView;

    public TitleGuideHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.tv_title);
        lineView = itemView.findViewById(R.id.title_line_view);
    }
}
