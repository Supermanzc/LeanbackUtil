package com.wt.leanbackutil.leankback;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.leanback.widget.HeaderItem;
import com.open.leanback.widget.Presenter;
import com.open.leanback.widget.Row;
import com.open.leanback.widget.RowHeaderPresenter;
import com.wt.leanbackutil.R;

/**
 * @author junyan
 * header头
 */

public class HeaderPresenter extends RowHeaderPresenter{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item,
                parent, false);
        return new HeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        HeaderItem headerItem = item == null ? null : ((Row) item).getHeaderItem();
        if (null != headerItem) {
            TextView tv = viewHolder.view.findViewById(R.id.title);
            tv.setText(headerItem.getName());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    public static class HeadViewHolder extends ViewHolder {
        public HeadViewHolder(View view) {
            super(view);
        }
    }

}
